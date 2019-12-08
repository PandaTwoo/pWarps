package br.com.pandatwo.pwarps.modules;

/*
   Essa é uma versão mais antiga de modulação, hoje é mais completa e melhor de trabalhar :)
   Só não alterei pois daria mais trabalho desnecessário pois essa versão não apresenta erros.
*/
public interface Module extends Comparable<Module> {

    void load();

    void unload();

    void reload();

    String getName();

    ModulePriority getPriority();

    @Override
    default int compareTo(Module module) {
        int thisPriority = this.getPriority().getValue();
        int anotherPriority = module.getPriority().getValue();
        if (thisPriority < anotherPriority) {
            return 1;
        } else if (thisPriority > anotherPriority) {
            return -1;
        }
        return 0;
    }
}
