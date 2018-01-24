/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.ExtensionList;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;

/**
 *
 * @author Thomas Vandenberghe
 */
public class RdfFileTypeLoader extends UniFileLoader {
       
    public RdfFileTypeLoader() {
        super("be.naturalsciences.bmdc.ears.ontology.rdf.RdfFileTypeDataObject");
        ExtensionList list = new ExtensionList();
        list.addExtension("rdf");
        setExtensions(list);
        setDisplayName("Rdf Files");
    }

    @Override
    public MultiDataObject createMultiObject(FileObject fo) throws DataObjectExistsException, IOException {
        MultiDataObject mo = null;
        if (fo != null) {
            mo = (MultiDataObject) DataObject.find(fo);
        }
        return mo;
    }

}
