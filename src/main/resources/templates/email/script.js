javascript
// Configurações da API
const API_BASE_URL = 'http://localhost:8080/api';

// Elementos da DOM
const cadastroScreen = document.getElementById('cadastro-screen');
const loginScreen = document.getElementById('login-screen');
const cadastroForm = document.getElementById('cadastro-form');
const loginForm = document.getElementById('login-form');
const successModal = document.getElementById('success-modal');
const notification = document.getElementById('notification');
const notificationMessage = document.getElementById('notification-message');

// Máscaras de input
function aplicarMascaras() {
    // Máscara de CPF
    const cpfInput = document.getElementById('cpf');
    cpfInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        }
        e.target.value = value;
    });

    // Máscara de telefone
    const telefoneInput = document.getElementById('telefone');
    telefoneInput.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            value = value.replace(/(\d{2})(\d)/, '($1) $2');
            value = value.replace(/(\d{5})(\d)/, '$1-$2');
        }
        e.target.value = value;
    });

    // Validação de força da senha
    const passwordInput = document.getElementById('password');
    passwordInput.addEventListener('input', function(e) {
        validarForcaSenha(e.target.value);
    });

    // Validação de confirmação de senha
    const confirmPasswordInput = document.getElementById('confirm-password');
    confirmPasswordInput.addEventListener('input', function() {
        validarConfirmacaoSenha();
    });
}

// Validação de força da senha
function validarForcaSenha(senha) {
    const strengthFill = document.getElementById('strength-fill');
    let strength = 0;

    // Remove classes anteriores
    strengthFill.className = 'strength-fill';

    if (senha.length >= 8) strength++;
    if (/[a-z]/.test(senha)) strength++;
    if (/[A-Z]/.test(senha)) strength++;
    if (/[0-9]/.test(senha)) strength++;
    if (/[^A-Za-z0-9]/.test(senha)) strength++;

    // Aplica classe baseada na força
    if (strength <= 1) {
        strengthFill.classList.add('strength-weak');
    } else if (strength <= 3) {
        strengthFill.classList.add('strength-medium');
    } else if (strength <= 4) {
        strengthFill.classList.add('strength-strong');
    } else {
        strengthFill.classList.add('strength-very-strong');
    }
}

// Validação de confirmação de senha
function validarConfirmacaoSenha() {
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const errorElement = document.getElementById('confirm-password-error');

    if (confirmPassword && password !== confirmPassword) {
        errorElement.textContent = 'As senhas não coincidem';
        document.getElementById('confirm-password').classList.add('error');
        return false;
    } else {
        errorElement.textContent = '';
        document.getElementById('confirm-password').classList.remove('error');
        return true;
    }
}

// Validação de CPF
function validarCPF(cpf) {
    cpf = cpf.replace(/\D/g, '');

    if (cpf.length !== 11 || /^(\d)\1+$/.test(cpf)) {
        return false;
    }

    let soma = 0;
    for (let i = 0; i < 9; i++) {
        soma += parseInt(cpf.charAt(i)) * (10 - i);
    }

    let resto = soma % 11;
    let digito1 = resto < 2 ? 0 : 11 - resto;

    if (digito1 !== parseInt(cpf.charAt(9))) {
        return false;
    }

    soma = 0;
    for (let i = 0; i < 10; i++) {
        soma += parseInt(cpf.charAt(i)) * (11 - i);
    }

    resto = soma % 11;
    let digito2 = resto < 2 ? 0 : 11 - resto;

    return digito2 === parseInt(cpf.charAt(10));
}

// Validação de email
function validarEmail(email) {
    const allowedDomains = [
        'gmail.com', 'outlook.com', 'yahoo.com', 'hotmail.com',
        'icloud.com', 'bol.com.br', 'live.com', 'msn.com'
    ];

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        return false;
    }

    const domain = email.split('@')[1].toLowerCase();
    return allowedDomains.includes(domain);
}

// Validação de telefone
function validarTelefone(telefone) {
    const telefoneLimpo = telefone.replace(/\D/g, '');
    return telefoneLimpo.length === 11;
}

// Validação de senha
function validarSenha(senha) {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&].*$/;
    return senha.length >= 8 && regex.test(senha);
}

// Mostrar notificação
function mostrarNotificacao(mensagem, tipo = 'success') {
    notification.className = `notification ${tipo}`;
    notificationMessage.textContent = mensagem;
    notification.classList.remove('hidden');

    setTimeout(() => {
        fecharNotificacao();
    }, 5000);
}

// Fechar notificação
function fecharNotificacao() {
    notification.classList.add('hidden');
}

// Mostrar tela de cadastro
function mostrarCadastro() {
    loginScreen.classList.remove('active');
    cadastroScreen.classList.add('active');
    cadastroForm.reset();
    limparErros();
}

// Mostrar tela de login
function mostrarLogin() {
    cadastroScreen.classList.remove('active');
    loginScreen.classList.add('active');
    loginForm.reset();
    limparErros();
    successModal.classList.remove('active');
}

// Limpar mensagens de erro
function limparErros() {
    document.querySelectorAll('.error-message').forEach(el => {
        el.textContent = '';
    });
    document.querySelectorAll('.error').forEach(el => {
        el.classList.remove('error');
    });
}

// Mostrar loading
function mostrarLoading(formType) {
    const loadingElement = document.getElementById(`loading-${formType}`);
    const submitButton = document.getElementById(`${formType}-btn`);

    loadingElement.classList.add('active');
    submitButton.disabled = true;
}

// Esconder loading
function esconderLoading(formType) {
    const loadingElement = document.getElementById(`loading-${formType}`);
    const submitButton = document.getElementById(`${formType}-btn`);

    loadingElement.classList.remove('active');
    submitButton.disabled = false;
}

// Cadastro de usuário
async function cadastrarUsuario(dados) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dados)
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || 'Erro ao cadastrar usuário');
        }

        return result;
    } catch (error) {
        throw error;
    }
}

// Login de usuário
async function loginUsuario(dados) {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dados)
        });

        const result = await response.json();

        if (!response.ok) {
            throw new Error(result.message || 'Erro ao fazer login');
        }

        return result;
    } catch (error) {
        throw error;
    }
}

// Event Listener para formulário de cadastro
cadastroForm.addEventListener('submit', async function(e) {
    e.preventDefault();

    const formData = new FormData(this);
    const dados = {
        nome: formData.get('nome'),
        email: formData.get('email'),
        cpf: formData.get('cpf'),
        telefone: formData.get('telefone'),
        password: formData.get('password')
    };

    // Validações
    let valido = true;

    // Validação de nome
    if (!dados.nome || dados.nome.length < 2) {
        document.getElementById('nome-error').textContent = 'Nome deve ter pelo menos 2 caracteres';
        document.getElementById('nome').classList.add('error');
        valido = false;
    }

    // Validação de email
    if (!validarEmail(dados.email)) {
        document.getElementById('email-error').textContent = 'E-mail inválido ou domínio não permitido';
        document.getElementById('email').classList.add('error');
        valido = false;
    }

    // Validação de CPF
    if (!validarCPF(dados.cpf)) {
        document.getElementById('cpf-error').textContent = 'CPF inválido';
        document.getElementById('cpf').classList.add('error');
        valido = false;
    }

    // Validação de telefone
    if (!validarTelefone(dados.telefone)) {
        document.getElementById('telefone-error').textContent = 'Telefone inválido';
        document.getElementById('telefone').classList.add('error');
        valido = false;
    }

    // Validação de senha
    if (!validarSenha(dados.password)) {
        document.getElementById('password-error').textContent = 'Senha deve ter pelo menos 8 caracteres com letras maiúsculas, minúsculas, números e símbolos';
        document.getElementById('password').classList.add('error');
        valido = false;
    }

    // Validação de confirmação de senha
    if (!validarConfirmacaoSenha()) {
        valido = false;
    }

    if (!valido) return;

    // Enviar dados
    try {
        mostrarLoading('cadastro');

        const resultado = await cadastrarUsuario(dados);

        esconderLoading('cadastro');
        successModal.classList.add('active');

    } catch (error) {
        esconderLoading('cadastro');
        mostrarNotificacao(error.message, 'error');

        // Tratamento de erros específicos
        if (error.message.includes('Email já cadastrado')) {
            document.getElementById('email-error').textContent = error.message;
            document.getElementById('email').classList.add('error');
        } else if (error.message.includes('CPF já cadastrado')) {
            document.getElementById('cpf-error').textContent = error.message;
            document.getElementById('cpf').classList.add('error');
        } else if (error.message.includes('Telefone já cadastrado')) {
            document.getElementById('telefone-error').textContent = error.message;
            document.getElementById('telefone').classList.add('error');
        }
    }
});

// Event Listener para formulário de login
loginForm.addEventListener('submit', async function(e) {
    e.preventDefault();

    const formData = new FormData(this);
    const dados = {
        login: formData.get('login'),
        password: formData.get('password')
    };

    // Validações básicas
    if (!dados.login || !dados.password) {
        mostrarNotificacao('Preencha todos os campos', 'error');
        return;
    }

    try {
        mostrarLoading('login');

        const resultado = await loginUsuario(dados);

        esconderLoading('login');

        // Salvar token (em produção, usar secure cookies)
        localStorage.setItem('token', resultado.data.token);
        localStorage.setItem('user', JSON.stringify(resultado.data));

        mostrarNotificacao('Login realizado com sucesso!', 'success');

        // Redirecionar para dashboard (substitua pela URL desejada)
        setTimeout(() => {
            window.location.href = '/dashboard.html';
        }, 2000);

    } catch (error) {
        esconderLoading('login');
        mostrarNotificacao(error.message, 'error');
    }
});

// Inicialização
document.addEventListener('DOMContentLoaded', function() {
    aplicarMascaras();

    // Verificar se já está logado
    const token = localStorage.getItem('token');
    if (token) {
        // Se já tem token, redireciona para dashboard
        window.location.href = '/dashboard.html';
    }

    // Mostrar cadastro por padrão
    mostrarCadastro();
});

// Funções globais para os botões
window.mostrarCadastro = mostrarCadastro;
window.mostrarLogin = mostrarLogin;
window.fecharNotificacao = fecharNotificacao;
