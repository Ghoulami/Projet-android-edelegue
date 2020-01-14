package android.example.edelegue.StudentModule;

public class Post {
    private String id;
    private String auteur;
    private String body;
    private String datetime;
    private String fichier;
    private String objet;
    private String file_name;

    public Post(String id, String auteur, String body, String datetime, String fichier, String objet,String file_name) {
        this.id = id;
        this.auteur = auteur;
        this.body = body;
        this.datetime = datetime;
        this.fichier = fichier;
        this.objet = objet;
        this.file_name = file_name;
    }

    public Post() {
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getFichier() {
        return fichier;
    }

    public void setFichier(String fichier) {
        this.fichier = fichier;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }
}
