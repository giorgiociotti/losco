package com.generation.models;

public class Armi extends Entity {

    private String nome;
    private int caricatore;
    private String calibro;
    private boolean automatica;
    private String colore;
    private Integer idCriminale; // nullable

    public Armi() {}

    public Armi(String nome, int caricatore, String calibro,
                boolean automatica, String colore, Integer idCriminale) {
        this.nome = nome;
        this.caricatore = caricatore;
        this.calibro = calibro;
        this.automatica = automatica;
        this.colore = colore;
        this.idCriminale = idCriminale;
    }

    public String getNome()           { return nome; }
    public void setNome(String nome)  { this.nome = nome; }

    public int getCaricatore()              { return caricatore; }
    public void setCaricatore(int c)        { this.caricatore = c; }

    public String getCalibre()              { return calibro; }
    public void setCalibre(String calibro)  { this.calibro = calibro; }

    public boolean isAutomatica()               { return automatica; }
    public void setAutomatica(boolean automatica){ this.automatica = automatica; }

    public String getColore()             { return colore; }
    public void setColore(String colore)  { this.colore = colore; }

    public Integer getIdCriminale()               { return idCriminale; }
    public void setIdCriminale(Integer idCrim)    { this.idCriminale = idCrim; }

    @Override
    public String toString() {
        String prop = (idCriminale != null) ? "criminale id=" + idCriminale : "senza proprietario";
        return String.format("[%d] %s | caricatore: %d | calibro: %s | automatica: %s | colore: %s | %s",
                id, nome, caricatore, calibro, automatica ? "sì" : "no", colore, prop);
    }
}
