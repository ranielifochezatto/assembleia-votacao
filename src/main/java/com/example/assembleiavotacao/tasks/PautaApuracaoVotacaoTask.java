package com.example.assembleiavotacao.tasks;

import com.example.assembleiavotacao.domains.Pauta;
import com.example.assembleiavotacao.services.PautaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class PautaApuracaoVotacaoTask {

    private final PautaService pautaService;

    @Scheduled(initialDelayString = "PT30S", fixedDelayString = "PT30S") // delay inicial de 30 segundos, executa a cada 30 segundos
    public void apurarVotacao() {
        log.debug("into apurarVotacao method");
        List<Pauta> pautas = pautaService.getPautasParaApurar();

        if(pautas != null){
            log.debug("pautas para apurar: {}", pautas.size());
            for(Pauta pauta : pautas){
                try {
                    pautaService.apurarPautaByIdNewTransaction(pauta.getId());
                    log.debug("apuracao da pauta {}: sucesso", pauta.getId());
                }catch(Exception ex){
                    log.debug("apuracao da pauta {}: falha -> {}", pauta.getId(), ex.getMessage());
                }
            }
        }
        log.debug("out apurarVotacao method");
    }

}
