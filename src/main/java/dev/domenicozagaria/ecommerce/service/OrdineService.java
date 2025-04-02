package dev.domenicozagaria.ecommerce.service;

import dev.domenicozagaria.ecommerce.dao.dto.ClienteDTO;
import dev.domenicozagaria.ecommerce.dao.dto.OrdineDTO;
import dev.domenicozagaria.ecommerce.dao.dto.ProdottoDTO;
import dev.domenicozagaria.ecommerce.dao.entity.ClienteEntity;
import dev.domenicozagaria.ecommerce.dao.entity.OrdineEntity;
import dev.domenicozagaria.ecommerce.dao.entity.OrdineProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.entity.ProdottoEntity;
import dev.domenicozagaria.ecommerce.dao.enumeration.StatoOrdine;
import dev.domenicozagaria.ecommerce.dao.repository.OrdineRepository;
import dev.domenicozagaria.ecommerce.exception.OrdineNotFoundException;
import dev.domenicozagaria.ecommerce.exception.OrdineStatoConsegnatoException;
import dev.domenicozagaria.ecommerce.exception.QuantitaExceedStockException;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdineService {

    private final OrdineRepository ordineRepository;

    private final ClienteService clienteService;
    private final ProdottoService prodottoService;

    @Transactional
    public OrdineDTO insertOrdine(int clienteId, OrdineDTO ordineDto) {
        ClienteEntity cliente = clienteService.getClienteById(clienteId);
        List<ProdottoDTO> prodottiDto = ordineDto.prodotti();
        Set<Integer> prodottiIds = prodottiDto.stream()
                .map(ProdottoDTO::id)
                .collect(Collectors.toSet());
        List<ProdottoEntity> prodotti = prodottoService.getProdottiByIds(prodottiIds);
        Map<Integer, Integer> mapIdProdottoQuantitaScelta = prodottiDto.stream()
                .collect(Collectors.toMap(ProdottoDTO::id, ProdottoDTO::quantita));
        Map<Integer, Integer> mapIdProdottoStockResiduo = prodotti.stream()
                .collect(Collectors.toMap(ProdottoEntity::getId, ProdottoEntity::getStock));
        OrdineEntity ordine = new OrdineEntity();
        ordine.setCliente(cliente);
        ordine.setStatoOrdine(StatoOrdine.ORDINATO);
        List<OrdineProdottoEntity> ordineProdotti = prodotti.stream()
                .map(prodotto -> {
                    OrdineProdottoEntity ordineProdotto = new OrdineProdottoEntity();
                    ordineProdotto.setOrdine(ordine);
                    ordineProdotto.setProdotto(prodotto);
                    int quantitaScelta = mapIdProdottoQuantitaScelta.get(prodotto.getId());
                    int stockResiduo = mapIdProdottoStockResiduo.get(prodotto.getId());
                    if (quantitaScelta > stockResiduo) {
                        throw new QuantitaExceedStockException();
                    }
                    ordineProdotto.setQuantita(quantitaScelta);
                    return ordineProdotto;
                })
                .collect(Collectors.toList());
        ordine.setProdotti(ordineProdotti);
        OrdineEntity saved = ordineRepository.saveAndFlush(ordine);
        prodottoService.updateStock(mapIdProdottoQuantitaScelta, prodotti);
        return saved.toDto();
    }

    public Page<OrdineDTO> searchOrdiniByClienteExample(@Nullable ClienteDTO body, Pageable pageable) {
        Page<OrdineEntity> ordini = null;
        if (body != null) {
            Set<Integer> clientiIds = clienteService.searchCliente(body, pageable).getContent().stream()
                    .map(ClienteDTO::id)
                    .collect(Collectors.toSet());
            ordini = ordineRepository.findAllByClienteIdIn(clientiIds, pageable);
        } else {
            ordini = ordineRepository.findAllBy(pageable);
        }
        return ordini.map(OrdineEntity::toDto);
    }

    public void updateStatoOrdine(int ordineId, StatoOrdine statoOrdine) {
        OrdineEntity ordine = ordineRepository.findById(ordineId)
                .orElseThrow(OrdineNotFoundException::new);
        ordine.setStatoOrdine(statoOrdine);
        ordineRepository.save(ordine);
    }

    public void deleteOrdine(int ordineId) {
        OrdineEntity ordine = ordineRepository.findById(ordineId)
                .orElseThrow(OrdineNotFoundException::new);
        if (ordine.getStatoOrdine().equals(StatoOrdine.CONSEGNATO))
            throw new OrdineStatoConsegnatoException();
        ordineRepository.deleteById(ordineId);
    }

}
