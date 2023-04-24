/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

import java.io.File;
import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
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

    public MultiDataObject createMultiObject(String path) throws DataObjectExistsException, IOException {
        FileObject fo = FileUtil.toFileObject(new File(path));
        return createMultiObject(fo);
    }

    public MultiDataObject createMultiObject(File f) throws DataObjectExistsException, IOException {
        FileObject fo = FileUtil.toFileObject(f);
        return createMultiObject(fo);
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
