/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.naturalsciences.bmdc.ears.ontology;

/**
 *
 * @author Thomas Vandenberghe
 */
public class OntologyServices {

    public static final String listQuery = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2:<http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
            + "SELECT DISTINCT ?s\n"
            + "WHERE \n"
            + "{\n"
            + "%s\n"
            + " }\n"
            + "ORDER BY ASC(?s) ";

    public static final String listTermQuery = "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
            + "PREFIX skos:<http://www.w3.org/2004/02/skos/core#>\n"
            + "PREFIX ears2:<http://ontologies.ef-ears.eu/ears2/1#>\n"
            + "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
            + "PREFIX dc:<http://purl.org/dc/elements/1.1/>\n"
            + "SELECT DISTINCT ?s\n"
            + "WHERE \n"
            + "{\n"
            + "?s a skos:Concept.\n"
            + "?s dc:identifier ?id\n"
            + "FILTER (regex(?id, \"ears\", \"i\") )\n"
            + " }\n"
            + "ORDER BY ASC(?s) ";

    /*public static int getLastConceptIdOfType(Class cls, File ontologyFile) throws Exception {
     OntologyModel model = null;
     try {
     model = new OntologyModel<ToolCategory>(ontologyFile, true);
     } catch (FileNotFoundException ex) {
     Exceptions.printStackTrace(ex);
     } catch (OWLOntologyCreationException ex) {
     Exceptions.printStackTrace(ex);
     }
     return getLastConceptIdOfType(cls, model);

     }*/
    /*public static int getLastConceptIdOfType(Class cls, IOntologyModel model) throws Exception {

        ConceptMD md = (ConceptMD) Constants.EARS_CLASSNAMES.get(cls);
        if (md != null) {

            String qry = String.format(listQuery, md.sparqlWhere);

            ResultSet res = model.query(qry);
            int prevId = 0;
            while (res.hasNext()) {
                QuerySolution row = res.next();
                String uri = row.get("s").asNode().getURI();//asResource().getURI();
                String[] parts = uri.split("#");
                String idS = null;
                if (parts.length > 1) {
                    idS = parts[1].replaceAll(md.abbreviation.toLowerCase() + "_", "");

                    int id = 0;
                    if (isNumber(idS)) {
                        id = Integer.parseInt(idS);
                    }
                    if (id > prevId) {
                        prevId = id;
                    }
                }
            }
            return prevId;
        } else {
            throw new IllegalArgumentException("Method called for a class cls that doesn't have a sparql sentence in ConceptMD.");
        }
    }*/

    public static boolean isNumber(String s) {
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    /*public static int getLastTermId(File ontologyFile) throws Exception {
     OntologyModel model = null;

     try {
     model = new OntologyModel<ToolCategory>(ontologyFile, true);
     } catch (FileNotFoundException ex) {
     Exceptions.printStackTrace(ex);
     } catch (OWLOntologyCreationException ex) {
     Exceptions.printStackTrace(ex);
     }
     return getLastTermId(model);
     }*/
    /*public static int getLastTermId(IOntologyModel model) throws Exception {

        ResultSet res = model.query(listTermQuery);
        int prevId = 0;
        while (res.hasNext()) {
            QuerySolution row = res.next();
            String uri = row.get("s").asNode().getURI();//asResource().getURI();
            String idS = uri.split("#")[1].replaceAll("concept_", "");
            int id = Integer.parseInt(idS);
            if (id > prevId) {
                prevId = id;
            }
        }
        return prevId;
    }*/

}
