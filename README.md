# ECOMMERCE 4 EURIS

## Requisiti

Per avviare il servizio in locale, assicurati di avere installato Docker, Docker Compose, Maven e Java (JDK 21 o superiore).

## Profili
Sono stati dichiarati e utilizzati due profili:
* ***dev***: per gli sviluppi locali, utilizza il docker-compose per l'istanza database mysql ed è attivo di default
* ***prod***: per eventuale build tramite Dockerfile

## Avvio

**Avvia l'applicazione Spring Boot con Maven (il file [compose.yaml](compose.yaml) viene letto ed eseguito in automatico dal servizio, disattivato con profilo *prod*):**

```bash
  mvn spring-boot:run
```

Il server espone le API sulla porta 18080 (configurabile nel file [application-dev.properties](src/main/resources/application-dev.properties)).

### Esempio di endpoint

```http://localhost:18080/api/v1/clienti```

## Configurazione

* **File di configurazione**: Il file [application-dev.properties](src/main/resources/application-dev.properties) contiene le impostazioni di configurazione per l'ambiente locale, inclusa la porta del server (18080 di default) e le impostazioni di connessione al database.
* **Path**: La path base è ```/api``` definita nel file [application.properties](src/main/resources/application.properties)
* **Docker Compose**: Il file [compose.yaml](compose.yaml) definisce il servizio database utile allo sviluppo e allo start in ambiente locale.

## Database

* **MySQL**: Il database utilizzato è MySQL v8.4.
* **Spring Data**: Il servizio **NON** utilizza Native Query

## Containerizzazione
* **Dockerfile**: è possibile effettuare il build del micro-servizio utilizzando docker: il [Dockerfile](Dockerfile) è impostato per il build con 
profilo ***prod***.

## Testing

* **Unit**: sono stati implementati unit test con JUnit5, Mockito e Spring Boot Test
* **Integration**: sono stati implementati integration test con Spring Boot Test e TestContainers per la connessione a database.