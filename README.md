# Listify

**Listify** è una web application realizzata come progetto universitario per la materia **Processo e Sviluppo del Software**.  

Il progetto è stato sviluppato utilizzando il framework **Spring MVC**, con l'obiettivo di approfondire il pattern architetturale **Model-View-Controller (MVC)** e le sue applicazioni pratiche nello sviluppo di applicazioni web.

# Breve Descrizione dell'app

**Listify** è stata progettata per la gestione di to-do list. Le sue principali funzionalità includono:

- Registrazione, autenticazione ed eliminazione degli utenti.  
- Creazione, rinomina ed eliminazione di una o più to-do list.  
- Creazione, modifica ed eliminazione di attività associate alle to-do list.  
- Visualizzazione di un grafico a torta che fornisce una panoramica dello stato delle attività per una to-do list (numero di attività To Do, In Progress, e Completed).  

# Guida all'installazione

1. **Installazione di XAMPP**:
    - Scaricare [XAMPP](https://www.apachefriends.org/it/download.html) per eseguire il database in locale.
    - Una volta installato, avviare i servizi `Apache` e `MySQL`.
    - Importare il database tramite [questo script](https://gitlab.com/Alessandro-Isceri/listify/-/blob/main/listify.sql).

2. **Installazione di Spring Tools Suite**:
    - Scaricare e installare [Spring Tools Suite per Eclipse](https://spring.io/tools).

3. **Clonazione del repository**:
    - Clonare il repository Git del progetto oppure scaricarne il contenuto.
    - Importare il progetto in Eclipse.

4. **Configurazione del progetto**:
    - Aprire le `Properties` del progetto:
        - **Java Compiler**: impostare `JDK compliance` su Use compliance from execution environment.
        - **Project Facets**: convertire il progetto in _faceted form_ e selezionare:
            - `JavaScript 1.0`.
            - `Java 17`.
            - `Dynamic Web Module 5.0`.
    - Applicare le modifiche e salvare.

5. **Esecuzione dei test di integrazione**:
    - Assicurarsi che il database sia in esecuzione (dovrebbe essere già stato avviato durante il primo passaggio).
    - Eseguire il comando `Run As > Maven Install`.
    - Assicurarsi che la build abbia successo.

6. **Avvio del server**:
    - Eseguire il comando `Run As > Run On Server` selezionando il server Tomcat v11.0. 
    - Se il comando non è disponibile, occorre configurarlo manualmente:
        - `Run As > Run Configurations > Tomcat v11.0 Server at localhost`.
    - In caso di errore, verificare che le dipendenze di Maven vengano importate correttamente:
        - `Properties > Deployment Assembly > Add > Java Build Path Entries > Next > Maven Dependencies > Finish`.
    - Salvare e chiudere.
    - Riavviare il Server.

7. **Accesso all'applicazione**:
    - Una volta avviato il server, accedere all'applicazione tramite il browser all'indirizzo: [http://localhost:8080/listify/](http://localhost:8080/listify/).

# Maggiori informazioni
Per informazioni più dettagliate, consultare la relazione, ovvero il file [`Relazione_Progetto_PSS___Alessandro_Isceri.pdf
`](https://gitlab.com/Alessandro-Isceri/listify/-/blob/main/Relazione_Progetto_PSS___Alessandro_Isceri.pdf?ref_type=heads).