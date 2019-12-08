package br.com.pandatwo.pwarps.modules;

import br.com.pandatwo.pwarps.modules.impl.ConfigModule;
import br.com.pandatwo.pwarps.modules.impl.MessagesModule;
import br.com.pandatwo.pwarps.modules.impl.UsersModule;
import br.com.pandatwo.pwarps.modules.impl.WarpsModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

     /*
        Essa é uma versão mais antiga de modulação, hoje é mais completa e melhor de trabalhar :)
        Só não alterei pois daria mais trabalho desnecessário pois essa versão não apresenta erros.
     */

public class Modules {

    private static Modules instance;
    private List<Module> modules;

    public Modules() {
        this.modules = new ArrayList<>();
        this.modules.add(new WarpsModule());
        this.modules.add(new UsersModule());
        this.modules.add(new MessagesModule());
        this.modules.add(new ConfigModule());
        Collections.sort(modules);
    }

    public Optional<Module> getModule(String moduleName) {
        Module module = null;
        for (Module modules : modules) {
            if (modules.getName().equalsIgnoreCase(moduleName)) {
                module = modules;
                break;
            }
        }
        return Optional.ofNullable(module);
    }

    public void reloadAllModules() {
        modules.forEach(Module::reload);
    }

    public void loadAllModules() {
        modules.forEach(Module::load);
    }

    public void unloadAllModules() {
        modules.forEach(Module::unload);
    }

    public static Modules getInstance() {
        if (instance == null)
            instance = new Modules();
        return instance;
    }
}
