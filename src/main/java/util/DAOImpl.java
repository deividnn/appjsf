/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;

public class DAOImpl<T> implements DAO<T>, Serializable {

    private final Class<T> classe;
    private final Session sessao;

    public DAOImpl(Session sessao, Class<T> classe) {
        super();
        this.sessao = sessao;
        this.classe = classe;

    }

    @Override
    public void salvar(T t) {
        this.sessao.save(t);
    }

    @Override
    public void atualizar(T t) {
        this.sessao.update(t);
    }

    @Override
    public void excluir(T t) {
        this.sessao.delete(t);
    }

    @Override
    public T carregar(String hql) {
        return (T) this.sessao.createQuery(hql).setCacheable(true).uniqueResult();
    }

    @Override
    public List<T> listar(String hql) {
        return this.sessao.createQuery(hql).setCacheable(true).list();
    }

}
