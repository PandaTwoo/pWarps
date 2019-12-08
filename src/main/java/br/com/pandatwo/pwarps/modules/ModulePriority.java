package br.com.pandatwo.pwarps.modules;

import lombok.Getter;

     /*
        Essa é uma versão mais antiga de modulação, hoje é mais completa e melhor de trabalhar :)
        Só não alterei pois daria mais trabalho desnecessário pois essa versão não apresenta erros.
     */

public enum ModulePriority {
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    HIGHEST(4);

    @Getter
    private int value;

    ModulePriority(int value) {
        this.value = value;
    }


}
