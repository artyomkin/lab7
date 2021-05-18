package server.commandHandler.commands;

import java.util.Objects;

/**
 * Abstract class that contains name and description
 * **/
public abstract class AbstractCommand implements Executable {
    private String name;
    private String description;
    public AbstractCommand(){
        this.name = "unknown command";
        this.description = "uknown description";
    }
    public AbstractCommand(String name, String description){
        this.name = name;
        this.description = description;
    }
    /**
     * @return Name of command
     * **/
    public String getName(){
        return this.name;
    }
    /**
     * @return Description of command
     * **/
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractCommand that = (AbstractCommand) o;
        return name.equals(that.name) &&
                description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
