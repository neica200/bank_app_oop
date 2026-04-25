## Aplicație bancară
Acțiuni posibile:
  1. Creează un client nou
  2. Șterge un client existent
  3. Deschide un cont bancar
  4. Închide un cont bancar
  5. Afișează toate conturile unui client
  6. Caută cont după IBAN
  7. Depune bani într-un cont
  8. Transferă bani între conturi
  9. Afișează soldul unui cont
  10. Afișează istoricul tranzacțiilor unui cont
  11. Generează un extras de cont
  12. Adaugă un card la un cont
  13. Blochează un card
  14. Deblochează un card
  15. Afișează toate cardurile unui cont și statusul lor
  16. Calculează dobânda pentru conturile de economii
  17. Afișează detaliile unui card
  18. Setează PIN pentru card
  19. Schimbă PIN-ul unui card
  20. Efectuează plată cu cardul
  21. Limitează suma zilnica de tranzacționare
  22. Afișează tranzacțiile realizate cu un card
  23. Dezactivează definitiv un card
Obiecte:
- Client
   - Cont
     -  ContCurent
     -  ContEconomii
  - Card
  - Tranzacție
     + Transfer
        - TransferInterBancar
        - TransferIntraBancar
     + Plata
  - ExtrasCont
  - IBAN
  - Banca
 
