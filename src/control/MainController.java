package control;

import model.Edge;
import model.Graph;
import model.List;
import model.Vertex;

/**
 * Created by Jean-Pierre on 12.01.2017.
 */
public class MainController {

    //Attribute

    //Referenzen
    private Graph allUsers;

    public MainController(){
        allUsers = new Graph();
        createSomeUsers();
    }

    /**
     * Fügt Personen dem sozialen Netzwerk hinzu.
     */
    private void createSomeUsers(){
        insertUser("Ulf");
        insertUser("Silent Bob");
        insertUser("Dörte");
        insertUser("Ralle");
        befriend("Silent Bob", "Ralle");
        befriend("Dörte", "Ralle");
    }

    /**
     * Fügt einen Nutzer hinzu, falls dieser noch nicht existiert.
     * @param name
     * @return true, falls ein neuer Nutzer hinzugefügt wurde, sonst false.
     */
    public boolean insertUser(String name){
        //TODO 05: Nutzer dem sozialen Netzwerk hinzufügen.
        if(allUsers.getVertex(name) == null){
            allUsers.addVertex(new Vertex(name));
            return true;
        }
        return false;
    }

    /**
     * Löscht einen Nutzer, falls dieser existiert. Alle Verbindungen zu anderen Nutzern werden ebenfalls gelöscht.
     * @param name
     * @return true, falls ein Nutzer gelöscht wurde, sonst false.
     */
    public boolean deleteUser(String name){
        //TODO 07: Nutzer aus dem sozialen Netzwerk entfernen.
        Vertex remove = allUsers.getVertex(name);
        if(remove != null){
            allUsers.removeVertex(remove);
            return true;
        }
        return false;
    }

    /**
     * Falls Nutzer vorhanden sind, so werden ihre Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @return
     */
    public String[] getAllUsers(){
        //TODO 06: String-Array mit allen Nutzernamen erstellen.
        List<Vertex> help = allUsers.getVertices();
        int counter = 0;
        help.toFirst();
        while(help.hasAccess()) {
            counter++;
            help.next();
        }
        String[] result = new String[counter];
        help.toFirst();
        for(int i = 0; help.hasAccess(); i++){
            result[i] = help.getContent().getID();
            help.next();
        }
        return result;
    }

    /**
     * Falls der Nutzer vorhanden ist und Freunde hat, so werden deren Namen in einem String-Array gespeichert und zurückgegeben. Ansonsten wird null zurückgegeben.
     * @param name
     * @return
     */
    public String[] getAllFriendsFromUser(String name){
        //TODO 09: Freundesliste eines Nutzers als String-Array erstellen.
        if( allUsers.getVertex(name) != null){
            List<Vertex> neighbours = allUsers.getNeighbours(new Vertex(name));
            int counter = 0;
            while(neighbours.hasAccess()){
                counter++;
                neighbours.next();
            }

            String[] result = new String[counter];
            neighbours.toFirst();
            counter = 0;

            for(int i= 0; neighbours.hasAccess(); i++){
                result[i] = neighbours.getContent().getID();
                neighbours.next();
            }

            return result;
        }
        return null;
    }

    /**
     * Bestimmt den Zentralitätsgrad einer Person im sozialen Netzwerk, falls sie vorhanden ist. Sonst wird -1.0 zurückgegeben.
     * Der Zentralitätsgrad ist der Quotient aus der Anzahl der Freunde der Person und der um die Person selbst verminderten Anzahl an Nutzern im Netzwerk.
     * Gibt also den Prozentwert an Personen im sozialen Netzwerk an, mit der die Person befreundet ist.
     * @param name
     * @return
     */
    public double centralityDegreeOfUser(String name){
        //TODO 10: Prozentsatz der vorhandenen Freundschaften eines Nutzers von allen möglichen Freundschaften des Nutzers.
          if(allUsers.getVertex(name) != null){
            double numberFriends = getAllFriendsFromUser(name).length;
            double numberUser = getAllUsers().length-1;

            return numberFriends/numberUser;
        }
        return -1;
    }

    /**
     * Zwei Nutzer des Netzwerkes gehen eine Freundschaft neu ein, falls sie sich im Netzwerk befinden und noch keine Freunde sind.
     * @param name01
     * @param name02
     * @return true, falls eine neue Freundeschaft entstanden ist, ansonsten false.
     */
    public boolean befriend(String name01, String name02){
        //TODO 08: Freundschaften schließen.
        if(allUsers.getVertex(name01) != null && allUsers.getVertex(name02) != null && allUsers.getEdge(allUsers.getVertex(name01), allUsers.getVertex(name02)) == null) {
            Edge edge = new Edge(allUsers.getVertex(name01), allUsers.getVertex(name02), 0);
            allUsers.addEdge(edge);
            return true;
        }
        return false;
    }

    /**
     * Zwei Nutzer beenden ihre Freundschaft, falls sie sich im Netzwerk befinden und sie befreundet sind.
     * @param name01
     * @param name02
     * @return true, falls ihre Freundschaft beendet wurde, ansonsten false.
     */
    public boolean unfriend(String name01, String name02){
        //TODO 11: Freundschaften beenden.
        if(allUsers.getVertex(name01) != null && allUsers.getVertex(name02) != null){
            Edge edge = allUsers.getEdge(new Vertex(name01),new Vertex(name02));
            allUsers.removeEdge(edge);
            if(edge != null){
                return true;
            }
        }
        return false;
    }

    /**
     * Bestimmt die Dichte des sozialen Netzwerks und gibt diese zurück.
     * Die Dichte ist der Quotient aus der Anzahl aller vorhandenen Freundschaftsbeziehungen und der Anzahl der maximal möglichen Freundschaftsbeziehungen.
     * @return
     */
    public double dense() {
        //TODO 12: Dichte berechnen.
        List<Edge> allEdges = allUsers.getEdges();
        int edgeCount = 0;
        allEdges.toFirst();

        while (allEdges.hasAccess()) {
            edgeCount++;
            allEdges.next();
        }

        List<Vertex> pos = allUsers.getVertices();
        double vertex = 0;
        double vertexHelp = 0;
        pos.toFirst();

        while (pos.hasAccess()) {
            vertex += vertexHelp;
            vertexHelp++;
            pos.next();
        }

        return edgeCount / vertex;
    }

    /**
     * Gibt die möglichen Verbindungen zwischen zwei Personen im sozialen Netzwerk als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return
     */
    public String[] getLinksBetween(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 13: Schreibe einen Algorithmus, der mindestens eine Verbindung von einem Nutzer über Zwischennutzer zu einem anderem Nutzer bestimmt. Happy Kopfzerbrechen!
            List<String> list = getLinksBetweenRec(name01,name02);
            int counter = 0;
            while(list.hasAccess()){
                counter++;
            }
            String[] result = new String[counter];
            for(int i = 0; list.hasAccess();i++){
                result[i] = list.getContent();
                list.next();
            }
        }
        return null;
    }

    private List<String> getLinksBetweenRec (String name01, String name02) {
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);

        if (user01 != null && user02 != null) {
            List<String> helpList = new List<>();
            helpList.append(name01);
            // at the end
            if (user01 == user02) {
                return helpList;
            }
            // recursively search for all neighbours
            List<Vertex> allNeighbours = allUsers.getNeighbours(user01);
            allNeighbours.toFirst();
            while (allNeighbours.hasAccess()) {
                // if we haven't been there
                if (!allNeighbours.getContent().isMarked()) {
                    allNeighbours.getContent().setMark(true);
                    List<String> rec = getLinksBetweenRec(allNeighbours.getContent().getID(), name02);
                    // if the recursive call returned a result
                    if (rec != null) {
                        helpList.concat(rec);
                        return helpList;
                    }
                }
                allNeighbours.next();
            }
            // no result
            return null;
        }
        // faulty input
        return null;
    }
    /**
     * Gibt zurück, ob es keinen Knoten ohne Nachbarn gibt.
     * @return true, falls ohne einsame Knoten, sonst false.
     */
    public boolean someoneIsLonely(){
        //TODO 14: Schreibe einen Algorithmus, der explizit den von uns benutzten Aufbau der Datenstruktur Graph und ihre angebotenen Methoden so ausnutzt, dass schnell (!) iterativ geprüft werden kann, ob der Graph allUsers keine einsamen Knoten hat. Dies lässt sich mit einer einzigen Schleife prüfen.
        List<Vertex> help = allUsers.getVertices();
        if(!allUsers.isEmpty()){
            while(help.hasAccess()){
                if(allUsers.getNeighbours(help.getContent()).isEmpty()){
                    return true;
                }
                help.next();
            }
        }
        return false;
    }

    /**
     * Gibt zurück, ob vom ersten Knoten in der Liste aller Knoten ausgehend alle anderen Knoten erreicht also markiert werden können.
     * Nach der Prüfung werden noch vor der Rückgabe alle Knoten demarkiert.
     * @return true, falls alle Knoten vom ersten ausgehend markiert wurden, sonst false.
     */
    public boolean testIfConnected(){
        //TODO 15: Schreibe einen Algorithmus, der ausgehend vom ersten Knoten in der Liste aller Knoten versucht, alle anderen Knoten über Kanten zu erreichen und zu markieren.
        List<Vertex> allVertices = allUsers.getVertices();
        //Ich habe mich eintschieden, dass wenn leer => return true
        if(!allVertices.isEmpty()) {
            allVertices.toFirst();
            Vertex first = allVertices.getContent();
            allVertices.next();
            while(allVertices.hasAccess()) {
                if (getLinksBetween(first.getID(), allVertices.getContent().getID()) == null){
                    allUsers.setAllVertexMarks(false);
                    return false;
                }
                allUsers.getVertex(allVertices.getContent().getID()).setMark(true);
                allVertices.next();
            }
        }
        allUsers.setAllVertexMarks(false);
        return true;
    }
    /**
     * Gibt einen String-Array zu allen Knoten zurück, die von einem Knoten ausgehend erreichbar sind, falls die Person vorhanden ist.
     * Im Anschluss werden vor der Rückgabe alle Knoten demarkiert.
     * @return Alle erreichbaren Knoten als String-Array, sonst null.
     */
    public String[] transitiveFriendships(String name){
        //TODO 16: Schreibe einen Algorithmus, der ausgehend von einem Knoten alle erreichbaren ausgibt.
        return null;
    }
    
    
    /**
     * Gibt eine kürzeste Verbindung zwischen zwei Personen des Sozialen Netzwerkes als String-Array zurück,
     * falls die Personen vorhanden sind und sie über eine oder mehrere Ecken miteinander verbunden sind.
     * @param name01
     * @param name02
     * @return Verbindung als String-Array oder null, falls es keine Verbindung gibt.
    */
    public String[] shortestPath(String name01, String name02){
        Vertex user01 = allUsers.getVertex(name01);
        Vertex user02 = allUsers.getVertex(name02);
        if(user01 != null && user02 != null){
            //TODO 17: Schreibe einen Algorithmus, der die kürzeste Verbindung zwischen den Nutzern name01 und name02 als String-Array zurückgibt. Versuche dabei einen möglichst effizienten Algorithmus zu schreiben.
        }
        return null;
    }

}
