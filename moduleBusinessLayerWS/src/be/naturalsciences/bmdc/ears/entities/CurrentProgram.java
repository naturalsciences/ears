/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.entities;

/**
 *
 * @author Thomas Vandenberghe
 */
public class CurrentProgram implements IProgram, CurrentSingleton<IProgram> {

    private ProgramBean currentProgram;

    private static final CurrentProgram instance = new CurrentProgram();

    private CurrentProgram() {
    }

    public ProgramBean getConcept() {
        return currentProgram;
    }

    public static CurrentProgram getInstance(ProgramBean currentProgram) {
       /* if (currentProgram == null) {
            throw new IllegalArgumentException("Program can't be null.");
        }*/
        instance.currentProgram = currentProgram;
        return instance;
    }

    @Override
    public boolean isLegal() {
        return true;
    }
}
