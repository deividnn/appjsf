/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.List;

/**
 *
 * @author DeividNn
 */
public interface DAO<T> {

    public void salvar(T t);

    public void atualizar(T t);

    public void excluir(T t);

    public T carregar(String hql);

    public List<T> listar(String hql);
}
