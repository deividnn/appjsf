/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores;

import entidades.Usuario;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import util.DAOImpl;
import util.Util;

/**
 *
 * @author DeividNn
 */
public class UsuarioControlador implements Serializable {

    private final DAOImpl<Usuario> dao;

    public UsuarioControlador() {
        this.dao = new DAOImpl<>(Util.pegarSessao(), Usuario.class);
    }

    public boolean salvar(Usuario usuario) {
        boolean ok;
        try {
            if (usuario.getId() == null) {

                String hql = "SELECT u FROM Usuario u"
                        + " WHERE u.usuario='" + usuario.getUsuario() + "'"
                        + " OR u.email='" + usuario.getEmail() + "'";

                Usuario verifica = this.dao.carregar(hql);

                if (verifica == null) {
                    usuario.setSenha(Util.converterStringEmMD5(usuario.getSenha()));
                    usuario.setUsuarioAlteracao(Util.usuarioLogado());
                    usuario.setDataCadastro(new Date());
                    usuario.setDataAlteracao(new Date());
                    this.dao.salvar(usuario);
                    Util.criarAviso("usuario salvo com sucesso");
                    ok = true;
                } else {
                    Util.criarAvisoErro("usuario ja existe");
                    ok = false;
                }
            } else {

                String hql = "SELECT u FROM Usuario u"
                        + " WHERE (u.usuario='" + usuario.getUsuario() + "'"
                        + " OR u.email='" + usuario.getEmail() + "')"
                        + " AND u.id!=" + usuario.getId() + "";

                Usuario verifica = this.dao.carregar(hql);

                if (verifica == null) {
                    usuario.setUsuarioAlteracao(Util.usuarioLogado());
                    usuario.setDataAlteracao(new Date());
                    this.dao.atualizar(usuario);
                    Util.criarAviso("usuario atualizado com sucesso");
                    ok = true;
                } else {
                    Util.criarAvisoErro("usuario ja existe");
                    ok = false;
                }
            }
        } catch (Exception e) {
            Util.criarAvisoErro("erro");
            ok = false;
        }
        return ok;
    }

    public boolean excluir(Usuario usuario) {
        boolean ok;
        try {
            this.dao.excluir(usuario);
            Util.criarAviso("usuario excluido com sucesso");
            ok = true;
        } catch (Exception e) {
            Util.criarAvisoErro("erro");
            ok = false;
        }
        return ok;
    }

    public List<Usuario> listar(String hql) {
        return this.dao.listar(hql);
    }

    public Usuario carregar(String hql) {
        return this.dao.carregar(hql);
    }

}
