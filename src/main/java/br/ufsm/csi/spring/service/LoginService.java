package br.ufsm.csi.spring.service;


import br.ufsm.csi.spring.model.User;

public class LoginService {

    public DataBaseServiceUser db_user;


    public LoginService() {
        this.db_user = new DataBaseServiceUser();
    }

    public boolean autentication(String email, String password) throws Exception {

        User user = db_user.searchEmail(email);

        if (user == null) {
            throw new Exception("Usuário não encontrado.");
        }

        if (!user.getPassword().equals(password)) {
            throw new Exception("Senha incorreta.");
        }

        return true;
    }

}
