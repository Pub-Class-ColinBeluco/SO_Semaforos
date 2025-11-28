# Aplicação prática de semáforos
## Integrantes

| Nome | RA | Projeto |
|----------|----------|----------|
| Ana Catarina   | 41383   | [Filósofos na mesa]()   |
| Colin Beluco   | 40975   | [Barbeiro dorminhoco](https://github.com/Pub-Class-ColinBeluco/SO_Semaforos/tree/main/Semaforos%20na%20pratica%20-%20problema%20do%20barbeiro%20boca%20aberta)   |
| Joyce Lidia   | 41397   | [Produtor Consumidor](https://github.com/Pub-Class-ColinBeluco/SO_Semaforos/tree/main/Semaforos%20na%20pratica%20-%20produtor%20consumidor)   |

---

O endereço IP privado é o que aparecerá na saída da execução dos códigos. Os exemplos abaixos foram em um único host da nuvem. A captura abaixo comprova o IP privado

![Captura de tela do IP privado do host na AWS: ](https://github.com/Pub-Class-ColinBeluco/SO_Semaforos/blob/main/Capturas%20de%20tela/Inst%C3%A2ncia%20e%20seu%20ip%20privado.png)

### Problema do barbeiro dorminhoco
O código foi executado dentro da pasta do projeto, especificando o caminho para os arquivos out e o de input de arquivo (txt na raíz dos projetos)

**Entrada**
```bash
java -cp out src/BarbeiroDorminhoco.java /input.txt
```

**Saída**
```
Executando em: ip-172-31-17-10
IP: 172.31.17.10
----------------------------------------------
[23:23:03.196263603] [Cliente-Thread: 17] Chegou na barbearia.
[23:23:03.196486446] [Cliente-Thread: 17] Sentou na espera. Cadeiras livres: 3.
[23:23:03.196736093] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 36 ms.
[23:23:03.233224240] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 1.
[23:23:03.233830735] [Cliente-Thread: 17] Saindo. Cadeiras livres: 4.
[23:23:03.234319448] [Cliente-Thread: 18] Chegou na barbearia.
[23:23:03.234541894] [Cliente-Thread: 18] Sentou na espera. Cadeiras livres: 3.
[23:23:03.234762937] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 33 ms.
[23:23:03.262594657] [Cliente-Thread: 19] Chegou na barbearia.
[23:23:03.263337652] [Cliente-Thread: 19] Sentou na espera. Cadeiras livres: 2.
[23:23:03.268135833] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 2.
[23:23:03.268524014] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 34 ms.
[23:23:03.268709764] [Cliente-Thread: 18] Saindo. Cadeiras livres: 3.
[23:23:03.279312847] [Cliente-Thread: 20] Chegou na barbearia.
[23:23:03.279647330] [Cliente-Thread: 20] Sentou na espera. Cadeiras livres: 2.
[23:23:03.293686765] [Cliente-Thread: 21] Chegou na barbearia.
[23:23:03.294034941] [Cliente-Thread: 21] Sentou na espera. Cadeiras livres: 1.
[23:23:03.303092707] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 3.
[23:23:03.304102033] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 41 ms.
[23:23:03.304331640] [Cliente-Thread: 19] Saindo. Cadeiras livres: 2.
[23:23:03.317990001] [Cliente-Thread: 22] Chegou na barbearia.
[23:23:03.318371122] [Cliente-Thread: 22] Sentou na espera. Cadeiras livres: 1.
[23:23:03.333312280] [Cliente-Thread: 23] Chegou na barbearia.
[23:23:03.333514738] [Cliente-Thread: 23] Sentou na espera. Cadeiras livres: 0.
[23:23:03.344129574] [Cliente-Thread: 24] Chegou na barbearia.
[23:23:03.344333457] [Cliente-Thread: 24] REJEITADO. Sem cadeiras, foi embora. Rejeitados: 1.
[23:23:03.345655148] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 4.
[23:23:03.345935815] [Cliente-Thread: 20] Saindo. Cadeiras livres: 1.
[23:23:03.346054297] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 55 ms.
[23:23:03.358463056] [Cliente-Thread: 25] Chegou na barbearia.
[23:23:03.358631333] [Cliente-Thread: 25] Sentou na espera. Cadeiras livres: 0.
[23:23:03.375893929] [Cliente-Thread: 26] Chegou na barbearia.
[23:23:03.376267798] [Cliente-Thread: 26] REJEITADO. Sem cadeiras, foi embora. Rejeitados: 2.
[23:23:03.401666433] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 5.
[23:23:03.402029633] [Cliente-Thread: 21] Saindo. Cadeiras livres: 1.
[23:23:03.402006178] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 49 ms.
[23:23:03.411160450] [Cliente-Thread: 27] Chegou na barbearia.
[23:23:03.411334863] [Cliente-Thread: 27] Sentou na espera. Cadeiras livres: 0.
[23:23:03.440514792] [Cliente-Thread: 28] Chegou na barbearia.
[23:23:03.440702585] [Cliente-Thread: 28] REJEITADO. Sem cadeiras, foi embora. Rejeitados: 3.
[23:23:03.451452581] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 6.
[23:23:03.451812862] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 38 ms.
[23:23:03.451940297] [Cliente-Thread: 22] Saindo. Cadeiras livres: 1.
[23:23:03.460904904] [Cliente-Thread: 29] Chegou na barbearia.
[23:23:03.461087312] [Cliente-Thread: 29] Sentou na espera. Cadeiras livres: 0.
[23:23:03.490150140] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 7.
[23:23:03.490758660] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 32 ms.
[23:23:03.491032266] [Cliente-Thread: 23] Saindo. Cadeiras livres: 1.
[23:23:03.494176990] [Cliente-Thread: 30] Chegou na barbearia.
[23:23:03.494327346] [Cliente-Thread: 30] Sentou na espera. Cadeiras livres: 0.
[23:23:03.523123267] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 8.
[23:23:03.523340582] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 42 ms.
[23:23:03.523440278] [Cliente-Thread: 25] Saindo. Cadeiras livres: 1.
[23:23:03.528890057] [Cliente-Thread: 31] Chegou na barbearia.
[23:23:03.529054044] [Cliente-Thread: 31] Sentou na espera. Cadeiras livres: 0.
[23:23:03.543295856] [Cliente-Thread: 32] Chegou na barbearia.
[23:23:03.543498502] [Cliente-Thread: 32] REJEITADO. Sem cadeiras, foi embora. Rejeitados: 4.
[23:23:03.561444684] [Cliente-Thread: 33] Chegou na barbearia.
[23:23:03.561603049] [Cliente-Thread: 33] REJEITADO. Sem cadeiras, foi embora. Rejeitados: 5.
[23:23:03.565597401] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 9.
[23:23:03.565824085] [Cliente-Thread: 27] Saindo. Cadeiras livres: 1.
[23:23:03.565966568] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 58 ms.
[23:23:03.577822164] [Cliente-Thread: 34] Chegou na barbearia.
[23:23:03.578105688] [Cliente-Thread: 34] Sentou na espera. Cadeiras livres: 0.
[23:23:03.611305742] [Cliente-Thread: 35] Chegou na barbearia.
[23:23:03.611514317] [Cliente-Thread: 35] REJEITADO. Sem cadeiras, foi embora. Rejeitados: 6.
[23:23:03.624167905] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 10.
[23:23:03.624389108] [Cliente-Thread: 29] Saindo. Cadeiras livres: 1.
[23:23:03.624562436] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 57 ms.
[23:23:03.645166374] [Cliente-Thread: 36] Chegou na barbearia.
[23:23:03.645364244] [Cliente-Thread: 36] Sentou na espera. Cadeiras livres: 0.
[23:23:03.681801290] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 11.
[23:23:03.682025192] [Cliente-Thread: 30] Saindo. Cadeiras livres: 1.
[23:23:03.682212835] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 39 ms.
[23:23:03.721686863] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 12.
[23:23:03.721948898] [Cliente-Thread: 31] Saindo. Cadeiras livres: 2.
[23:23:03.722015513] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 53 ms.
[23:23:03.775357760] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 13.
[23:23:03.775604536] [Barbeiro-Thread: 16] ️ Atendendo Cliente. Serviço de 49 ms.
[23:23:03.775709358] [Cliente-Thread: 34] Saindo. Cadeiras livres: 3.
[23:23:03.824836503] [Barbeiro-Thread: 16] Cliente atendido. Total atendidos: 14.
[23:23:03.825136438] [Cliente-Thread: 36] Saindo. Cadeiras livres: 4.

--- RESULTADOS FINAIS ---
Clientes Atendidos: 14
[23:23:05.677511081] [Barbeiro-Thread: 16] Encerrando o Barbeiro.
Clientes Rejeitados: 6
Utilização do Barbeiro: 22.60%
-------------------------
```
