package com.generation.service;

import com.generation.dao.ArmiDao;
import com.generation.dao.CriminaleDao;
import com.generation.models.Criminali;

import java.util.List;

public class CriminaleService {

    private CriminaleDao criminaleDao = new CriminaleDao();
    private ArmiDao armiDao = new ArmiDao();

    public void inserisci(Criminali c)          { criminaleDao.insert(c); }
    public List<Criminali> trovaTutti()         { return criminaleDao.findAll(); }
    public Criminali trovaById(int id)          { return criminaleDao.findById(id); }
    public List<Criminali> trovaByGruppo(String g){ return criminaleDao.findByGruppo(g); }
    public List<Criminali> trovaTuttiConArmi()  { return criminaleDao.findAllWithArmi(); }
    public void aggiorna(Criminali c)           { criminaleDao.update(c); }

    // Prima dissocia le armi (FK NO ACTION), poi elimina
    public void elimina(int id) {
        armiDao.dissociaTutteDiCriminale(id);
        criminaleDao.delete(id);
    }
}
