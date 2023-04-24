/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.netbeans.services;

/**
 *
 * @author thomas
 */
public interface Tester<C> {

    public boolean test(C a, C b);
}
