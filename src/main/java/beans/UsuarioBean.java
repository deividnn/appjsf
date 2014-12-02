/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import controladores.UsuarioControlador;
import entidades.Usuario;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.primefaces.event.FileUploadEvent;
import util.Util;

/**
 *
 * @author DeividNn
 */
@ManagedBean
@ViewScoped
public class UsuarioBean implements Serializable {

    private Usuario usuario;
    private Usuario usuarioSelecionado;
    private List<Usuario> usuarios;
    private List<Usuario> revisoes;
    private boolean ver;
    private List<String> colunas;
    private String colunaSelecionada;
    private String texto;
    private String tipoExportacao;

    @PostConstruct
    public void init() {
        listar();
        this.colunas = Util.atributosClasse(Usuario.class);
        this.colunaSelecionada = this.colunas.get(0);
        this.tipoExportacao = "pdf";
    }

    public String retornaFoto() {
        if (this.usuario != null) {
            if (this.usuario.getFoto() != null) {
                return "/resources/fotos/" + this.usuario.getFoto();
            } else {
                return "/resources/fotos/foto.gif";
            }
        }
        return "/resources/fotos/foto.gif";
    }

    public String retornaFoto(Usuario usu) {
        if (usu != null) {
            if (usu.getFoto() != null) {
                return "/resources/fotos/" + usu.getFoto();
            } else {
                return "/resources/fotos/foto.gif";
            }
        }
        return "/resources/fotos/foto.gif";
    }

    public void processarFoto(FileUploadEvent event) {

        ServletContext servletContext = (ServletContext) FacesContext.
                getCurrentInstance().getExternalContext().getContext();
        String absoluteDiskPath = servletContext.getRealPath("/resources/fotos/");
        File targetFolder = new File(absoluteDiskPath);
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();

        }

        try (InputStream inputStream = event.getFile().getInputstream()) {
            OutputStream out;
            out = new FileOutputStream(new File(targetFolder, event.getFile().getFileName()));
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
            this.usuario.setFoto(event.getFile().getFileName());

            Util.criarAviso("Foto processada!");
            Util.executarJavaScript("PF('dlgfoto').hide();");
            Util.atualizarForm("criar:pic");
        } catch (IOException ex) {
            Util.criarAvisoErro("ERRO:!" + ex);

        }

    }

    public void pesquisar() {

        String hql = "SELECT vo FROM Usuario vo"
                + " WHERE UPPER(CAST(vo." + this.colunaSelecionada + " as text)) "
                + " LIKE '" + this.texto.toUpperCase() + "%' "
                + " AND vo.nivel!='SUPER'"
                + " ORDER BY vo." + this.colunaSelecionada + " ASC";

        this.usuarios = new UsuarioControlador().listar(hql);

    }

    public String pegarStatus(String status) {
        switch (status) {
            case "A":
                return "Ativo";
            case "I":
                return "Inativo";
            default:
                return "Bloqueado";
        }
    }

    private void listar() {
        this.ver = false;
        this.revisoes = new ArrayList<>();
        this.usuarioSelecionado = new Usuario();
        this.usuarios = new ArrayList<>();
        String hql = "SELECT vo FROM Usuario vo WHERE vo.nivel!='SUPER'";
        this.usuarios = new UsuarioControlador().listar(hql);
    }

    public void salvar() {

        if (new UsuarioControlador().salvar(this.usuario)) {
            listar();
            Util.atualizarForm("usuario");
            Util.executarJavaScript("PF('dlgcriar').hide();");
        }
    }

    public void inserir() {
        this.ver = false;
        this.usuario = new Usuario();
        resetarFormulario();
    }

    public void editar() {
        this.ver = false;
        this.usuario = this.usuarioSelecionado;
        resetarFormulario();
    }

    public void excluir() {
        if (new UsuarioControlador().excluir(this.usuarioSelecionado)) {
            listar();
        }
    }

    public void ver() {
        this.ver = true;
        this.usuario = this.usuarioSelecionado;
        resetarFormulario();

    }

    public void verRevisoes() {
        this.revisoes = new ArrayList<>();
        this.usuario = this.usuarioSelecionado;
        resetarFormulario();

        AuditReader reader = AuditReaderFactory.get(Util.pegarSessao());

        List<Number> rev = reader.getRevisions(Usuario.class, this.usuario.getId());
        for (Number revisoe : rev) {
            Usuario o = reader.find(Usuario.class, this.usuario.getId(), revisoe);
            this.revisoes.add(o);
        }
    }

    public void exportar() {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletResponse response = (HttpServletResponse) context.getResponse();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        ServletContext servletContext = (ServletContext) context.getContext();
        String arquivoJasper = "/WEB-INF/relatorios/usuarios.jasper";
        try {
            Util.gerar(request, response, arquivoJasper, servletContext,
                    Util.pegarConexao(), this.tipoExportacao, "usuarios");
        } catch (IOException ex) {
            Logger.getLogger(UsuarioBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().responseComplete();

    }

    public void resetarFormulario() {
        Util.resetarFormulario("criar");
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getUsuarioSelecionado() {
        return usuarioSelecionado;
    }

    public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
        this.usuarioSelecionado = usuarioSelecionado;
    }

    public boolean isVer() {
        return ver;
    }

    public void setVer(boolean ver) {
        this.ver = ver;
    }

    public List<Usuario> getRevisoes() {
        return revisoes;
    }

    public void setRevisoes(List<Usuario> revisoes) {
        this.revisoes = revisoes;
    }

    public List<String> getColunas() {
        return colunas;
    }

    public void setColunas(List<String> colunas) {
        this.colunas = colunas;
    }

    public String getColunaSelecionada() {
        return colunaSelecionada;
    }

    public void setColunaSelecionada(String colunaSelecionada) {
        this.colunaSelecionada = colunaSelecionada;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getTipoExportacao() {
        return tipoExportacao;
    }

    public void setTipoExportacao(String tipoExportacao) {
        this.tipoExportacao = tipoExportacao;
    }

}
