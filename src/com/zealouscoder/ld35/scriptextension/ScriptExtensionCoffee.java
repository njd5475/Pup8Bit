package com.zealouscoder.ld35.scriptextension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptException;

import com.zealouscoder.ld35.Game;

public class ScriptExtensionCoffee extends ScriptExtension {
    private CompiledScript compiledScript;
    private Compilable     compiler;

    public ScriptExtensionCoffee(Game game) {
        super(game);
        compiler = (Compilable) engine;

        URL coffeeUri = getLocalResource("coffee-script.js");
        try {
            String coffee = getFile(coffeeUri.toURI());
            coffee += "\nCoffeeScript.compile(__source__, {bare: true});";
            compiledScript = compiler.compile(coffee);
        } catch (ScriptException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void runInitScript() throws FileNotFoundException, ScriptException {
        try {
            engine.eval(convertToJs(getFile("resources/init.coffee")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String convertToJs(String coffee) throws ScriptException {
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put("__source__", coffee);
        return compiledScript.eval(bindings).toString();
    }
    
    public String getFile(String filename) throws IOException {
        return getFile(Paths.get(filename).toUri());
    }

    public String getFile(URI uri) throws IOException {
        return new String(Files.readAllBytes(Paths.get(uri)));
    }
    
    public URL getLocalResource(String resource) {
        return this.getClass().getResource(resource);
    }
}
