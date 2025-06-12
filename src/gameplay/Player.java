package gameplay;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import backend.publicvars.PublicVar;

public class Player {
    private String name;

    private ArrayList<File> playerFiles;// TODO: Tree? This could have some limitations.
    private File playerFolder;
    private File globalVarsDir;
    private File publicVarsDir;

    private HashMap<String, PublicVar> publicVars;

    // Using this files must either be added in a valid order without fail or the
    // session must validate the arraylist
    // Could be validated by iterating over the arraylist and going back if there
    // are any extra files that have not been created
    // TODO: Create a validator method here or in the session class

    public Player(String name, String playerSpacePath) {
        this.name = name;

        playerFiles = new ArrayList<File>();

        playerFolder = new File(playerSpacePath + "\\" + name);
        playerFiles.add(playerFolder);

        globalVarsDir = new File(playerFolder.getAbsolutePath() + "\\" + "globalVars");
        playerFiles.add(globalVarsDir);

        publicVarsDir = new File(playerFolder.getAbsolutePath() + "\\" + "publicVars");
        playerFiles.add(publicVarsDir);

        publicVars = new HashMap<>();
    }

    public static Player fromFile(File playerFile) {
        // Add more functionality when theres more than a name
        return new Player(playerFile.getName(), playerFile.getParentFile().getPath());
    }

    public File[] files() {
        return playerFiles.toArray(new File[playerFiles.size()]);
    }

    public String getName() {
        return name;
    }

    public File getPlayerFolder() {
        return playerFolder;
    }

    public void addVariable(PublicVar var) {
        publicVars.put(var.getName(), var);
    }

    public Optional<PublicVar> getVariable(String name) {
        if (publicVars.get(name) == null) {
            if (new File(publicVarsDir.getPath() + "\\" + name).exists()) {
                PublicVar importedVar = PublicVar.fromFile(this, new File(publicVarsDir.getPath() + "\\" + name));
                publicVars.put(name, importedVar);
                return Optional.of(importedVar);
            } else {
                System.out.println("DEBUG: File is silly: " + publicVarsDir.getName() + "\\" + name);
                return Optional.empty();
            }
        }
        return Optional.ofNullable(publicVars.get(name));
    }

    @Override
    public boolean equals(Object other) {
        return ((Player) other).getName().equals(name);
    }

    @Override
    public String toString(){
        return getName();
    }
}
