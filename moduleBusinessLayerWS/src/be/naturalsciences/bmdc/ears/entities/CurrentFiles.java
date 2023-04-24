/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

import java.io.File;
import java.util.Set;

/**
 *
 * @author thomas
 */
public class CurrentFiles implements CurrentSingleton<Set> {

    private Set<File> files;

    private static final CurrentFiles instance = new CurrentFiles();

    private CurrentFiles() {
    }

    public Set<File> getConcept() {
        return files;
    }

    public static CurrentFiles getInstance(Set currentFiles) {
        if (currentFiles == null) {
            throw new IllegalArgumentException("currentFiles can't be null.");
        }
        instance.files = currentFiles;
        return instance;
    }

    @Override
    public String toString() {
        return files.toString();
    }

    /*@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrentFiles)) {
            return false;
        }
        CurrentFiles f = (CurrentFiles) o;
        return this.getFile().equals(f.getFile());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.file);
        return hash;
    }*/

}
