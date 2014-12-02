/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import entidades.Revisao;
import org.hibernate.envers.RevisionListener;

/**
 *
 * @author DeividNn
 */
public class CustomRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        Revisao revision = (Revisao) revisionEntity;
    }

}
