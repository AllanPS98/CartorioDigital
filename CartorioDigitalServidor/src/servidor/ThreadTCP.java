package servidor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import cliente.Cidadao;
import cliente.Documento;
import cliente.Protocolo;
import cliente.Transferencia;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author allan
 */
public class ThreadTCP implements Runnable {

    private final Socket cliente;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private static Handler han;

    ThreadTCP(Socket cliente, ObjectInputStream in, ObjectOutputStream out, Handler han) {
        this.cliente = cliente;
        this.in = in;
        this.out = out;
        ThreadTCP.han = han;
    }

    @Override
    public void run() {
        try {
            carregarDados();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ThreadTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        int protocoloAtual;
        while (true) {

            try {
                protocoloAtual = (int) input();
                System.out.println("Protocolo = "+ protocoloAtual);
                if (protocoloAtual == Protocolo.CADASTRAR_DOCUMENTO) {
                    String id = (String) input();
                    String login = (String) input();
                    String texto = (String) input();
                    boolean achou = false;
                    boolean podeCadastrar = true;
                    for (int i = 0; i < Handler.usuarios.size(); i++) {
                        if (login.equals(Handler.usuarios.get(i).getCpf())) {
                            for (int j = 0; j < Handler.usuarios.get(i).getDocumentos().size(); j++) {
                                if (Handler.usuarios.get(i).getDocumentos().get(j).getId().equals(id)) {
                                    podeCadastrar = false;
                                }
                            }
                            if (podeCadastrar) {
                                Documento doc = new Documento(id, Handler.usuarios.get(i).getNome(), login, texto);
                                String resultado = han.cadastrarDocumento(doc);
                                MulticastSender sender = new MulticastSender();
                                sender.outputDocumento(doc.toString());
                                achou = true;
                                output(resultado);
                            }
                        }
                    }
                    if (!achou) {
                        output("Erro");
                    }

                } else if (protocoloAtual == Protocolo.CADASTRAR_USUARIO) {
                    String nome = (String) input();
                    String cpf = (String) input();
                    String senha = (String) input();
                    String confirmaSenha = (String) input();
                    boolean cpfIgual = false;
                    if (senha.equals(confirmaSenha)) {
                        for (int i = 0; i < Handler.usuarios.size(); i++) {
                            if (cpf.equals(Handler.usuarios.get(i).getCpf())) {
                                cpfIgual = true;
                                output("Já existe alguém cadastrado com esse CPF.");
                            }
                        }
                    } else {
                        output("A senha está diferente no campo de confirmação.");
                    }
                    if (!cpfIgual) {
                        han.cadastrarUsuario(nome, cpf, senha);
                        MulticastSender sender = new MulticastSender();
                        sender.outputCidadao(new Cidadao(nome, cpf, senha).toString());
                        output("Cadastro efetuado com sucesso");
                    }
                } else if (protocoloAtual == Protocolo.LOGIN) {
                    String cpf = (String) input();
                    String senha = (String) input();
                    boolean podeLogar = false;
                    for (int i = 0; i < Handler.usuarios.size(); i++) {
                        if (cpf.equals(Handler.usuarios.get(i).getCpf())) {
                            if (han.verificarSenha(senha, Handler.usuarios.get(i).getSenha())) {
                                output("Login efetuado com sucesso.");
                                podeLogar = true;
                            }
                        }
                    }
                    if (!podeLogar) {
                        output("Erro ao logar, favor verificar senha e login.");
                    }

                } else if (protocoloAtual == Protocolo.TRANSFERIR_DOCUMENTO) {
                    String cpfs = (String) input();
                    Documento doc = (Documento) input();
                    float valorVenda = (float) input();
                    String[] particionada;
                    particionada = cpfs.split(";");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                    Date datax = new Date();
                    String data = sdf.format(datax);
                    Transferencia transf = new Transferencia(particionada[0], particionada[1], doc, data);
                    String resultado = han.cadastrarTransferencia(transf);
                    output(resultado);
                    
                } else if (protocoloAtual == Protocolo.SAIR) {
                    sair();
                    break;
                } else if (protocoloAtual == Protocolo.CARREGAR_LISTA_DOCUMENTOS) {
                    String cpf = (String) input();
                    
                    List<Documento> docs = new LinkedList();
                    for(int i = 0; i < Handler.usuarios.size(); i++){
                        if(cpf.equals(Handler.usuarios.get(i).getCpf())){
                            docs = Handler.usuarios.get(i).getDocumentos();
                            break;
                        }
                    }
                    output(docs);
                }else if(protocoloAtual == Protocolo.DECODIFICAR_DOC){
                    String texto = (String) input();
                    System.out.println("recebeu decodificar");
                    String textoDecod = han.decodificarTexto(texto);
                    System.out.println("decodificado = " + textoDecod);
                    output(textoDecod);
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Erro no protocolo recebido");
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ThreadTCP.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public Object input() throws IOException, ClassNotFoundException {

        try {

            InputStream tcpInput = new ObjectInputStream(in);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream obj = new ObjectOutputStream(bytes);

            try {

                obj.writeObject(((ObjectInputStream) tcpInput).readObject());
                bytes.toByteArray();
                Object mensagem = desserializarMensagem(bytes.toByteArray());

                return mensagem;
            } catch (ClassNotFoundException e) {
                Logger.getLogger(ThreadTCP.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (IOException e) {
            Logger.getLogger(ThreadTCP.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public void output(Object msg) throws IOException {
        out.flush();
        out.write(serializarMensagens(msg));
        out.reset();
    }

    private void sair() throws IOException {
        System.out.println("tentou sair");
        in.close();
        out.close();
        cliente.close();
        System.out.println("CLIENTE DESCONECTOU");
    }

    public byte[] serializarMensagens(Object mensagem) {
        ByteArrayOutputStream by = new ByteArrayOutputStream();
        try {
            ObjectOutput saida = new ObjectOutputStream(by);
            saida.writeObject(mensagem);
            saida.flush();
            return by.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ThreadTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object desserializarMensagem(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream mensagem = new ByteArrayInputStream(data);

        ObjectInput leitor = new ObjectInputStream(mensagem);
        return (Object) leitor.readObject();

    }

    public static void carregarDados() throws IOException, FileNotFoundException, ClassNotFoundException {
        han.lerArquivoSerial("dados\\usuarios");
    }
}
