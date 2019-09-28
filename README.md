# super-text-battle-royale


# TODO:
Lato gameplay:
- Definire statistiche dei giocatori/oggetti [SEMI-DONE]
- Creare un sistema di livelli
- Gestione logica delle mappe
    - Tipi di Tile ✔
    - Porte di entrata e uscita ✔
- Concordare sul tipo di gameplay generale da dare al gioco ✔
- Pathfinding per i giocatori ✔
- Sistema di movimento ✔


Lato grafico:
- Gestione delle mappe ✔
- Abbozzare una grafica ✔
- Animazioni
- Creare immagini per ogni personaggio/oggetto ✔

Lato gestione:
- Creare un sistema di Log decente 

GESTIONE TURNO:
ogni mappa esegue onTick di ogni giocatore vivo presente su di essa
se l'azione e' da visualizzare viene inquadrata la mappa e viene visualizzato il turno

COME FARE LO ZOOM
getAttention prendera' come parametro una lista di giocatori che andranno visualizzati
