package com.generation.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Criminali extends Entity {

    private String nome;
    private String cognome;
    private String alias;
    private LocalDate dob;
    private String gruppo;
    private List<Armi> armi = new ArrayList<>();

    public Criminali() {}

    public Criminali(String nome, String cognome, String alias, LocalDate dob, String gruppo) {
        this.nome = nome;
        this.cognome = cognome;
        this.alias = alias;
        this.dob = dob;
        this.gruppo = gruppo;
    }

    public String getNome()           { return nome; }
    public void setNome(String nome)  { this.nome = nome; }

    public String getCognome()              { return cognome; }
    public void setCognome(String cognome)  { this.cognome = cognome; }

    public String getAlias()            { return alias; }
    public void setAlias(String alias)  { this.alias = alias; }

    public LocalDate getDob()           { return dob; }
    public void setDob(LocalDate dob)   { this.dob = dob; }

    public String getGruppo()             { return gruppo; }
    public void setGruppo(String gruppo)  { this.gruppo = gruppo; }

    public List<Armi> getArmi()             { return armi; }
    public void setArmi(List<Armi> armi)    { this.armi = armi; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s (alias: %s) | nato: %s | gruppo: %s",
                id, nome, cognome, alias, dob, gruppo);
    }
}
