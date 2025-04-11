# 📍 Documentação: Permissões em Tempo de Execução
### Tema do App: Localização
- [Equipe](/TEAM.md)
- [Canva](https://www.canva.com/design/DAGkXoPaNHw/YbWoKMzJ-gmUH0Z_LDZ57Q/view)

## 🧾 Objetivo  
Esta documentação descreve como funciona o fluxo de solicitação de permissão de localização em tempo de execução em apps Android, utilizando o exemplo da localização do usuário (latitude e longitude).

---

## 🔐 1. Declaração de Permissão  
Antes de tudo, o app precisa declarar no Manifesto as permissões necessárias, como acesso à localização.  
> ⚠️ Isso **não garante o acesso imediato** — apenas sinaliza que o app irá solicitar essa permissão ao usuário durante o uso.

---

## ⏳ 2. Solicitação em Tempo de Execução  
Desde o Android 6.0 (API 23), algumas permissões são classificadas como **"perigosas"** (ex: localização, câmera, microfone).  
Isso significa que o app deve solicitar a permissão em tempo real, enquanto está sendo utilizado, e o usuário pode **aceitar ou negar**.

---

## 🙋‍♂️ 3. Resposta do Usuário  
Quando o sistema exibe o diálogo de permissão, o usuário pode:

- ✅ **Permitir**: o app poderá acessar a localização normalmente.  
- ❌ **Negar**: o app não terá acesso e precisa lidar com isso.  
- 🚫 **Negar com “Não perguntar novamente”**: o sistema não mostrará mais a caixa de diálogo automaticamente.  
  > O app deve orientar o usuário a liberar a permissão nas **Configurações do sistema**.
