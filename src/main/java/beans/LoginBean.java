/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import controladores.UsuarioControlador;
import entidades.Usuario;
import java.io.Serializable;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import util.Util;

/**
 *
 * @author DeividNn
 */
@ManagedBean
@ViewScoped
public class LoginBean implements Serializable {

    private Usuario login;

    @PostConstruct
    public void init() {
        this.login = new Usuario();
    }

    public void fazerLogin() {
        String senha = Util.converterStringEmMD5(this.login.getSenha());
        String hql = "SELECT vo FROM Usuario vo"
                + " WHERE vo.usuario='" + this.login.getUsuario() + "'"
                + " AND vo.senha='" + senha + "'";
        Usuario verifica = new UsuarioControlador().carregar(hql);
        if (verifica != null) {
            if (!verifica.getStatus().equals("A")) {
                Util.criarAvisoErro("Usuario bloqueado/inativo");
            } else {

                Util.criarObjetoDeSessao(verifica, "usuarioLogado");
                Util.redirecionarPagina("restrito/");
                this.login = new Usuario();
            }
        } else {
            Util.criarAvisoErro("Usuario ou senha incorreto");
        }
    }

    public void fazerLogout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        //redireciona para pagina login
        Util.redirecionarPagina("");
    }

    public void recuperarSenha() {
        String hql = "SELECT vo FROM Usuario vo"
                + " WHERE vo.email='" + this.login.getEmail() + "'";
        Usuario verifica = new UsuarioControlador().carregar(hql);
        if (verifica != null) {
            enviarEmail(verifica);
            Util.criarAviso("Senha enviada para o email");
            Util.atualizarForm(":senha");
            this.login = new Usuario();
        } else {
            Util.criarAvisoErro("Email nao cadastrado");
        }
    }

    private void enviarEmail(Usuario usuario) {
        Properties props = new Properties();
        /**
         * Parâmetros de conexão com servidor Hotmail
         */
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("deivid6291@hotmail.com", "senha");
                    }
                });
        //session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("deivid6291@hotmail.com")); //Remetente
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(usuario.getEmail())); //Destinatário(s)
            message.setSubject("Recuperacao de senha do sistema APPJSF");//Assunto
            message.setText("Voce solicitou sua senha atraves do sistema APPJSF - "
                    + ".Sua senha esta criptograda: " + usuario.getSenha() + " , use o site "
                    + " http://www.hashkiller.co.uk/md5-decrypter.aspx para descriptografar");
            /**
             * Método para enviar a mensagem criada
             *
             */
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } finally {

        }
    }

    public Usuario getLogin() {
        return login;
    }

    public void setLogin(Usuario login) {
        this.login = login;
    }

}
