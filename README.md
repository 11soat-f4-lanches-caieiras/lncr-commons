# Módulo Core

Este documento contém as configurações necessárias para utilizar o artefato `lncr-core` publicado no GitHub Packages em outros projetos.

## 1. Sobre o lncr-core
O `lncr-core` é um módulo que contém as regras de negócio, entidades do domínio, interfaces dos casos de uso da aplicação Lanches Caieiras e bibliotecas de uso comum.
Ele está publicado no GitHub Packages e pode ser utilizado como dependência em projetos Mave.


## 2. Utilizando lncr-core nos projetos
Para configuar o artefato em outros projetos, adicione no `pom.xml` do seu projeto:

```xml
<project>
<repositories>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>https://maven.pkg.github.com/11soat-f3-lanches-caieiras/lncr-core</url>
        <releases>
            <updatePolicy>always</updatePolicy>
        </releases>
    </repository>
</repositories>
...
<dependencyManagement>
   <dependencies>
       <dependency>
           <groupId>br.com.tp.lncr</groupId>
           <artifactId>core</artifactId>
           <version>2.0</version>
       </dependency>
   </dependencies>
</dependencyManagement>
...
</project>
```

## 3. Configuração do settings.xml do Maven

### Localização do Arquivo
- **Windows**: `C:\Users\{seu-usuario}\.m2\settings.xml`
- **Linux/Mac**: `~/.m2/settings.xml`

### Conteúdo Completo do settings.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
    
    <!-- Configuração de servidores para autenticação -->
    <servers>
        <server>
            <id>github</id>
            <username>${env.GITHUB_USERNAME}</username>
            <password>${env.GITHUB_TOKEN}</password>
        </server>
    </servers>
    
</settings>
```

## 4. Instruções para Configuração de Login e Senha

### Passo 1: Criar Personal Access Token (PAT)

1.  Seguir documentação GitHub:
   - [Como criar um personal access token (classic)](https://docs.github.com/pt/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens#como-criar-um-personal-access-token-classic)


2. **Seu PAT precisa ter os seguintes acessos**:
   - ✅ `read:packages` - Para baixar pacotes
   - ✅ `write:packages` - Para publicar pacotes (opcional, só se for desenvolvedor)
   - ✅ `repo` - Acesso ao repositório (se for repositório privado)

### Passo 2: Configurar as Credenciais

1. **Substitua os valores no settings.xml**:
   - `GITHUB_USERNAME`: Seu nome de usuário do GitHub
   - `GITHUB_TOKEN`: O token PAT que você acabou de gerar

2. **Exportar variáveis de ambiente (opcional)**:
   - Para evitar colocar diretamente no `settings.xml`, você pode usar variáveis de ambiente:

   No Linux/Mac:
   ```bash
   export GITHUB_USERNAME=seu_usuario_github
   export GITHUB_TOKEN=seu_token_github
   ```

   No Windows (PowerShell):
   ```powershell
   $env:GITHUB_USERNAME="seu_usuario_github"
   $env:GITHUB_TOKEN="seu_token_github"
   ```




### Passo 3: Testar a Configuração

Execute o seguinte comando para verificar se tudo está funcionando:

```bash
# Navegar até um projeto que usa o lncr-core
cd seu-projeto

# Limpar cache local e baixar dependências
mvn clean dependency:purge-local-repository

# Resolver dependências
mvn dependency:resolve

# Ou simplesmente compilar
mvn clean compile
```

## 5. Exemplo Completo de Uso

### Estrutura do projeto consumidor:

```
meu-projeto/
├── pom.xml
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── exemplo/
│                   └── MinhaClasse.java
└── ~/.m2/
    └── settings.xml
```

### pom.xml do projeto consumidor:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.exemplo</groupId>
    <artifactId>meu-projeto</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <repositories>
        <repository>
            <id>github</id>
            <name>GitHub Packages</name>
            <url>https://maven.pkg.github.com/11soat-f3-lanches-caieiras/lncr-core</url>
            <releases>
                <updatePolicy>always</updatePolicy>
            </releases>
        </repository>
    </repositories>
    
    <dependencies>
        <dependency>
            <groupId>br.com.tp.lncr</groupId>
            <artifactId>core</artifactId>
            <version>2.0</version>
        </dependency>
    </dependencies>
    
</project>
```

### Exemplo de uso em Java:

```java
package com.exemplo;

import br.com.tp.lncr.core.domain.entities.Cliente;
import br.com.tp.lncr.core.domain.valueobjects.CPF;

public class MinhaClasse {
    public static void main(String[] args) {
        try {
            CPF cpf = new CPF("12345678901");
            Cliente cliente = new Cliente("João Silva", "joao@email.com", cpf);
            System.out.println("Cliente criado: " + cliente.getNome());
        } catch (Exception e) {
            System.err.println("Erro ao criar cliente: " + e.getMessage());
        }
    }
}
```