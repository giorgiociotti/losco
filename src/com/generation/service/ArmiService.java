package com.generation.service;

import com.generation.dao.ArmiDao;
import com.generation.models.Armi;

import java.util.List;

public class ArmiService {

    private ArmiDao armiDao = new ArmiDao();

    public void inserisci(Armi a)                       { armiDao.insert(a); }
    public List<Armi> trovaTutte()                      { return armiDao.findAll(); }
    public Armi trovaById(int id)                       { return armiDao.findById(id); }
    public List<Armi> trovaByCriminale(int idCrim)      { return armiDao.findByCriminale(idCrim); }
    public List<Armi> trovaSenzaProprietario()          { return armiDao.findSenzaProprietario(); }
    public void aggiorna(Armi a)                        { armiDao.update(a); }
    public void elimina(int id)                         { armiDao.delete(id); }
    public void dissociaProprietario(int idArma)        { armiDao.dissociaProprietario(idArma); }

    public void assegnaCriminale(int idArma, int idCrim) {
        Armi a = armiDao.findById(idArma);
        if (a == null) { System.out.println("Arma non trovata."); return; }
        a.setIdCriminale(idCrim);
        armiDao.update(a);
    }
}
