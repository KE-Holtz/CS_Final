package gameplay;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import backend.Session;
import backend.publicvars.PublicVar;

public class Player {
    private final Session session; 

    private final String name;
    protected final String delimiter;

    private final ArrayList<File> playerFiles;// TODO: Tree? This could have some limitations.
    private final File playerFolder;
    private final File globalVarsDir;
    private final File publicVarsDir;

    private final HashMap<String, PublicVar> publicVars;

    public Player(String name, Session session) {
        this.name = name;
        delimiter = session.getDelimiter();
        this.session = session;

        playerFiles = new ArrayList<File>();

        playerFolder = new File(session.getPlayerSpacePath() + delimiter + name);
        playerFiles.add(playerFolder);

        globalVarsDir = new File(playerFolder.getAbsolutePath() + delimiter + "globalVars");
        playerFiles.add(globalVarsDir);

        publicVarsDir = new File(playerFolder.getAbsolutePath() + delimiter + "publicVars");
        playerFiles.add(publicVarsDir);

        publicVars = new HashMap<>();
    }

    public static Player fromFile(File playerFile, Session session) {
        // Add more functionality when theres more than a name
        return new Player(playerFile.getName(), session);
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
            if (new File(publicVarsDir.getPath() + delimiter + name).exists()) {
                PublicVar importedVar = PublicVar.fromFile(this, new File(publicVarsDir.getPath() + delimiter + name));
                publicVars.put(name, importedVar);
                return Optional.of(importedVar);
            } else {
                System.out.println("DEBUG: File is silly: " + publicVarsDir.getName() + delimiter + name);
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

    public String getDelimiter(){
        return delimiter;
    }
}
