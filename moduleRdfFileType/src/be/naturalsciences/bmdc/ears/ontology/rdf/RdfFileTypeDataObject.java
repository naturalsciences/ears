/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology.rdf;

//import be.naturalsciences.bmdc.ears.infobar.InfoBar;
import be.naturalsciences.bmdc.ears.ontology.OntologyDataObject;
import be.naturalsciences.bmdc.ears.ontology.OntologyFactory;
import be.naturalsciences.bmdc.ears.utils.Message;
import be.naturalsciences.bmdc.ears.utils.Messaging;
import be.naturalsciences.bmdc.ears.utils.NotificationThread;
import be.naturalsciences.bmdc.ears.utils.TaskListener;
import be.naturalsciences.bmdc.ontology.IOntologyModel;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap;
import be.naturalsciences.bmdc.ontology.writer.ScopeMap.Scope;
import gnu.trove.set.hash.THashSet;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import javax.swing.JEditorPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.StyledDocument;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.core.api.multiview.MultiViews;
import org.netbeans.spi.actions.AbstractSavable;
import org.openide.awt.ActionReferences;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.loaders.SaveAsCapable;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.openide.text.Line;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

@Messages({
    "LBL_RdfFileType_LOADER=Files of RdfFileType"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_RdfFileType_LOADER",
        mimeType = "application/rdf+xml",
        extension = {"rdf"}
)
@DataObject.Registration(
        mimeType = "application/rdf+xml",
        iconBase = "be/naturalsciences/bmdc/ears/ontology/rdf/rdf-16.png",
        displayName = "#LBL_RdfFileType_LOADER",
        position = 300
)
@ActionReferences({})
public class RdfFileTypeDataObject extends MultiDataObject implements OntologyDataObject, EditorCookie, NodeListener {//,SaveCookie {

    public static int nbFilesOpen = 0;

    public InstanceContent lookupContent;
    private final Lookup lookup;
    private boolean firstTime = true;

    private final FileObject fileObject;
    private OntologyNode node;

    private IOntologyModel ontModel;
    private ScopeMap scope;

    private Exception ontEx = null;
    private Set<ChangeListener> changeListeners = new THashSet<>();
    private Set<DirectoryChangeListener> directoryChangeListeners = new THashSet<>();
    //private Set<PropertyChangeListener> propertyChangeListeners = new THashSet<>();

    public Set<DirectoryChangeListener> getDirectoryChangeListeners() {
        return directoryChangeListeners;
    }

    /*public void setDirectoryChangeListeners(Set<DirectoryChangeListener> directoryChangeListeners) {
        this.directoryChangeListeners = directoryChangeListeners;
    }*/
    public FileObject getFileObject() {
        return fileObject;
    }

    @Override
    public File getFile() {
        return FileUtil.toFile(this.getFileObject());
    }

    @Override
    public IOntologyModel getOntModel() {
        return ontModel;
    }

    @Override
    public String getName() {
        return getFile().getName();
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public InstanceContent getLookupContent() {
        return lookupContent;
    }

    public void setLookupContent(InstanceContent lookupContent) {
        this.lookupContent = lookupContent;
    }

    public final ScopeMap getScope() {
        if (scope == null) {
            try {
                Map<String, String> staticStuff = IOntologyModel.getStaticStuff(getFile());
                scope = new ScopeMap(ScopeMap.Scope.valueOf(staticStuff.get(IOntologyModel.SCOPE)), staticStuff.get(IOntologyModel.SCOPEDTO));
            } catch (FileNotFoundException ex) {
                scope = null;
            }
        }
        return scope;
    }

    public RdfFileTypeDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException, FileNotFoundException, OWLOntologyCreationException {
        super(pf, loader);
        this.fileObject = pf;
        registerEditor("application/rdf+xml", true);
        setModified(false);

        this.lookupContent = new InstanceContent();
        this.lookup = new AbstractLookup(this.lookupContent);
        try {
            refresh(true);
        } catch (IOException ex) {
            ontEx = ex;
            throw ex;
        }

        this.lookupContent.add(this); //necessary for RdfFileTypeVisualElement
    }

    public void loadOntology() throws IOException {
        refresh(firstTime);
        firstTime = false;
    }

    public void refresh(boolean firstTime) throws IOException {
        this.ontModel = null;
        this.ontModel = OntologyFactory.getOntology(this);//new OntologyModel(FileUtil.toFile(fileObject), true,true);

        if (!firstTime) {
            for (ChangeListener cl : changeListeners) {
                ChangeEvent e = new ChangeEvent("OntologyModel was reloaded from file. Please rebuild nodes.");
                cl.stateChanged(e);
            }
        }

    }

    public void setNode(OntologyNode node) {
        this.node = node;
    }

    public OntologyNode getNode() {
        return node;
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @Override
    protected Node createNodeDelegate() {
        return new OntologyNode(this);
    }

    @Override
    public String toString() {
        return getName();
    }

    public String getNiceName() {

        File ontologyFile = getFile();
        if (ontologyFile != null) {
            if (ontEx != null) {
                return "(MALFORMED RDF) " + ontologyFile.getName();
            } else if (getScope() == null) {
                return "(NO SCOPE) " + ontologyFile.getName();
            } else {
                String scopedTo = getScope().getScopedTo();
                return "(" + getScope().getScopeString() + (scopedTo != null && scopedTo.isEmpty() ? ", " + scopedTo : "") + ") " + ontologyFile.getName();
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RdfFileTypeDataObject) {
            RdfFileTypeDataObject obj = (RdfFileTypeDataObject) o;
            if (obj.getFileObject() != null && this.getFileObject() != null) {
                return FileUtil.toFile(obj.getFileObject()).equals(FileUtil.toFile(this.getFileObject()));
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash ^= (FileUtil.toFile(this.getFileObject()) != null ? FileUtil.toFile(this.getFileObject()).hashCode() : 0);
        return hash;
    }

    @Override
    public void open() {
        nbFilesOpen++;
        try {
            loadOntology();
        } catch (Exception ex) {
            ontEx = ex;
            Messaging.report("The tree in the file " + this.fileObject.getPath() + " is corrupt and couldn't be opened", ex, this.getClass(), true);
        }
        if (ontEx == null) {
            try {
                TopComponent tc = MultiViews.createMultiView("application/rdf+xml", this);
                tc.setName(RdfFileTypeVisualElement.TCNAME);
                tc.setHtmlDisplayName(this.getName());
                tc.open();
                tc.requestActive();
            } catch (Exception e) {
                Messaging.report("There was a problem with this tree file. It has not been opened.", e, this.getClass(), true);
            }
        }

    }

    @Override
    public boolean close() {
        nbFilesOpen = 0;
        if (getLookup().lookup(RdfFileTypeDataObject.class) == null) {
            this.lookupContent.add(this);
        }
        return true;
    }

    @Override
    public Task prepareDocument() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StyledDocument openDocument() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StyledDocument getDocument() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveDocument() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JEditorPane[] getOpenedPanes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Line.Set getLineSet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void childrenAdded(NodeMemberEvent nme) {
        changeOccured();
    }

    @Override
    public void childrenRemoved(NodeMemberEvent nme) {
        changeOccured();
    }

    @Override
    public void childrenReordered(NodeReorderEvent nre) {
        changeOccured();
    }

    @Override
    public void nodeDestroyed(NodeEvent ne) {
        changeOccured();
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        changeOccured();
    }

    private void changeOccured() {
        RdfFileTypeDataObjectSavable savable = getLookup().lookup(RdfFileTypeDataObjectSavable.class);
        if (savable == null) {
            savable = new RdfFileTypeDataObjectSavable(this);
            lookupContent.add(savable);
        }
    }

    private void saveComplete() {
        RdfFileTypeDataObjectSavable savable = getLookup().lookup(RdfFileTypeDataObjectSavable.class);
        if (savable != null) {
            //savable = new RdfFileTypeDataObjectSavable(this);
            lookupContent.remove(savable);
        }
    }

    public void addChangeListener(ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    public void addDirectoryChangeListener(DirectoryChangeListener listener) {
        this.directoryChangeListeners.add(listener);
    }

    @Override
    public boolean isRenameAllowed() {
        return false;
    }

    public boolean isCorrect() {
        return this.getOntModel() != null && ontEx == null;
    }

    public interface IOntologySaver {

        boolean canContinueEditingAfterSave();

        boolean isFailed();
    }

    public class RdfFileTypeDataObjectSavable extends AbstractSavable implements SaveAsCapable, TaskListener {

        protected RdfFileTypeDataObject dobj;

        public RdfFileTypeDataObject getDobj() {
            return dobj;
        }

        public RdfFileTypeDataObjectSavable(RdfFileTypeDataObject dobj) {
            this.dobj = dobj;
            register();
        }

        @Override
        protected String findDisplayName() {
            return dobj.getName();
        }

        public class OntologySaver extends NotificationThread implements IOntologySaver {

            ProgressHandle progr;
            boolean failed = false;

            public OntologySaver(ProgressHandle progr) {
                this.progr = progr;
            }

            @Override
            public void doWork() {
                progr.start();
                progr.progress("Start save.");
                failed = !dobj.getOntModel().getNodes().save();

                try {
                    if (!failed) { //if we failed we want to show the nodes as they were before the failed save
                        dobj.refresh(false);
                    }
                } catch (IOException ex) {
                    Messaging.report("There was a problem reloading the trees after saving.", ex, RdfFileTypeDataObject.class, true);
                }
                //unregister();
                if (!failed) {
                    Messaging.report("The tree has been saved.", Message.State.GOOD, RdfFileTypeDataObject.class, true);
                }
                progr.finish();
            }

            @Override
            public boolean isFailed() {
                return failed;
            }

            @Override
            public boolean canContinueEditingAfterSave() {
                return false;
            }
        }

        public class OntologySaveAsser extends NotificationThread implements IOntologySaver {

            ProgressHandle progr;
            String name;
            FileObject destination;
            boolean failed = false;

            public OntologySaveAsser(ProgressHandle progr, FileObject destination, String name) {
                this.progr = progr;
                this.name = name;
                this.destination = destination;
            }

            @Override
            public void doWork() {
                progr.start();
                progr.progress("Start save as.");
                Path path = Paths.get(destination.getPath(), name);
                failed = !dobj.getOntModel().getNodes().saveAs(path);
                for (DirectoryChangeListener dcl : dobj.getDirectoryChangeListeners()) {
                    ActionEvent e = new ActionEvent(path, 0, "OntologyModel was saved as another file. Please rebuild nodes.");
                    dcl.actionPerformed(e);
                }

                /*                try {
                    //if we failed OR NOT we want to show the nodes as they were before the save as
                    //dobj.refresh(false);
                } catch (IOException ex) {
                    Messaging.report("There was a problem reloading the tree.", ex, RdfFileTypeDataObject.class, true);
                }*/
                if (!failed) {
                    Messaging.report("The tree has been saved as at " + path.toString(), Message.State.GOOD, RdfFileTypeDataObject.class, true);
                } else {
                    Messaging.report("There was a problem saving the tree.", Message.State.BAD, RdfFileTypeDataObject.class, true);
                }
                progr.finish();
            }

            @Override
            public boolean isFailed() {
                return failed;
            }

            @Override
            public boolean canContinueEditingAfterSave() {
                return true;
            }
        }

        @Override
        public void threadComplete(Runnable runner) {
            IOntologySaver saver = (IOntologySaver) runner;
            if (!saver.isFailed() && !saver.canContinueEditingAfterSave()) {
                dobj.lookupContent.remove(this);
                saveComplete();
                unregister();
            }
        }

        ProgressHandle progr;

        @Override
        protected void handleSave() throws IOException {
            progr = ProgressHandleFactory.createHandle("Saving tree...");
            OntologySaver saver = new OntologySaver(progr);
            saver.addListener(this);
            RequestProcessor.getDefault().post(saver);
            progr.finish();
        }

        @Override
        public void saveAs(FileObject destination, String name) throws IOException {
            progr = ProgressHandleFactory.createHandle("Saving tree (save as)...");
            OntologySaveAsser saver = new OntologySaveAsser(progr, destination, name);
            saver.addListener(this);
            RequestProcessor.getDefault().post(saver);
            progr.finish();
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof RdfFileTypeDataObjectSavable) {
                RdfFileTypeDataObjectSavable m = (RdfFileTypeDataObjectSavable) o;
                return getDobj() == m.getDobj();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.dobj.hashCode();
        }
    }

}
