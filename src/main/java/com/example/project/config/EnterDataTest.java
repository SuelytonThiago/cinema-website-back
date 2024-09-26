package com.example.project.config;

import com.example.project.domain.entities.Categories;
import com.example.project.domain.entities.Movies;
import com.example.project.domain.entities.Roles;
import com.example.project.domain.entities.Users;
import com.example.project.domain.repositories.CategoryRepository;
import com.example.project.domain.repositories.MovieRepository;
import com.example.project.domain.repositories.RoleRepository;
import com.example.project.domain.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Configuration
public class EnterDataTest implements CommandLineRunner {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Users users = new Users();
        users.setName("adm");
        users.setEmail("adm@example.com");
        users.setContactNumber("99940028922");
        users.setCpf("61254591010");
        users.setPassword(encoder.encode("senha123"));
        users.setProfileImg("https://cdn-icons-png.flaticon.com/512/3106/3106921.png");
        usersRepository.save(users);

        Users users1 = new Users();

        users1.setName("maria");
        users1.setEmail("maria@example.com");
        users1.setContactNumber("99940028922");
        users1.setCpf("87466407030");
        users1.setPassword(encoder.encode("senha123"));
        users1.setProfileImg("https://i0.wp.com/newdoorfiji.com/wp-content/uploads/2018/03/profile-img-1.jpg?ssl=1");
        usersRepository.save(users1);


        Roles roles = new Roles(null,"ROLE_ADMIN");
        Roles roles1 = new Roles(null,"ROLE_USER");
        Roles roles2 = new Roles(null,"ROLE_OWNER");
        roleRepository.saveAll(Arrays.asList(roles,roles1,roles2));

        users.getRoles().add(roles);
        users1.getRoles().add(roles1);
        usersRepository.saveAll(Arrays.asList(users,users1));

        var movie1 = new Movies();
        movie1.setImageUrl("https://play-lh.googleusercontent.com/9pxN3jrTbT04lAxYu5BL9kMvUmeR2WoMyA3lP78AKDSj6Z9hY8zXF1IaJ3tkxgUqvZE");
        movie1.setName("Velozes e furiosos 1");
        movie1.setDescription("Eles são os pilotos dos automóveis mais envenenados de Los Angeles. Em alucinantes rachas ilegais, eles rasgam as ruas em busca de emoção adrenalina, sem medir esforços nem perigos para conquistar sempre o primeiro lugar. Audazes e irresponsáveis, alguns destes corredores são suspeitos deformar uma quadrilha especializada em roubos de cargas de caminhões. Para investigar o caso, a polícia infiltra um piloto entre os marginais.");
        movie1.setReleaseData(LocalDate.parse("28/09/2001", formatter));
        movie1.setClassification("18");

        var movie2 = new Movies();
        movie2.setImageUrl("https://br.web.img2.acsta.net/medias/nmedia/18/89/43/82/20052140.jpg");
        movie2.setName("Os Vingadores");
        movie2.setDescription("Loki (Tom Hiddleston) retorna à Terra enviado pelos chitauri, uma raça alienígena que pretende dominar os humanos. Com a promessa de que será o soberano do planeta, ele rouba o cubo cósmico dentro de instalações da S.H.I.E.L.D. e, com isso, adquire grandes poderes. Loki os usa para controlar o dr. Erik Selvig (Stellan Skarsgard) e Clint Barton/Gavião Arqueiro (Jeremy Renner), que passam a trabalhar para ele. No intuito de contê-los, Nick Fury (Samuel L. Jackson) convoca um grupo de pessoas com grandes habilidades, mas que jamais haviam trabalhado juntas: Tony Stark/Homem de Ferro (Robert Downey Jr.), Steve Rogers/Capitão América (Chris Evans), Thor (Chris Hemsworth), Bruce Banner/Hulk (Mark Ruffalo) e Natasha Romanoff/Viúva Negra (Scarlett Johansson). Só que, apesar do grande perigo que a Terra corre, não é tão simples assim conter o ego e os interesses de cada um deles para que possam agir em grupo.");
        movie2.setReleaseData(LocalDate.parse("28/09/2012", formatter));
        movie2.setClassification("14");

        var movie3 = new Movies();
        movie3.setImageUrl("https://upload.wikimedia.org/wikipedia/pt/thumb/0/00/The_Karate_Kid_2010.jpg/232px-The_Karate_Kid_2010.jpg");
        movie3.setName("Karate Kid");
        movie3.setDescription("Dre Parker (Jaden Smith) se mudou com a mãe (Taraji P. Henson) para Pequim, devido ao trabalho dela. Logo ao chegar ele se interessa por Meiying (Han Wenwen), uma garota que conhece praticando violino na praça. A aproximação deles provoca a irritação de Cheng (Zhenwei Wang), que lhe dá uma surra usando a técnica do kung fu. A partir de então a vida de Dre se torna um inferno, já que passa a ser perseguido na escola por Cheng e seus colegas. Um dia, ao escapar deles, Dre é auxiliado pelo sr. Han (Jackie Chan), o zelador de seu prédio, que é também um mestre de kung fu.");
        movie3.setReleaseData(LocalDate.parse("27/08/2010", formatter));
        movie3.setClassification("10");

        var movie4 = new Movies();
        movie4.setImageUrl("https://br.web.img2.acsta.net/c_310_420/medias/nmedia/18/90/59/44/20103781.jpg");
        movie4.setName("As Crônicas de Nárnia ");
        movie4.setDescription("Lúcia (Georgie Henley), Susana (Anna Popplewell), Edmundo (Skandar Keynes) e Pedro (William Moseley) são quatro irmãos que vivem na Inglaterra, em plena 2ª Guerra Mundial. Eles vivem na propriedade rural de um professor misterioso, onde costumam brincar de esconde-esconde. Em uma de suas brincadeiras eles descobrem um guarda-roupa mágico, que leva quem o atravessa ao mundo mágico de Nárnia. Este novo mundo é habitado por seres estranhos, como centauros e gigantes, que já foi pacífico mas hoje vive sob a maldição da Feiticeira Branca, Jadis (Tilda Swinton), que fez com que o local sempre estivesse em um pesado inverno. Sob a orientação do leão Aslam, que governa Nárnia, as crianças decidem ajudar na luta para libertar este mundo do domínio de Jadis.");
        movie4.setReleaseData(LocalDate.parse("09/12/2005", formatter));
        movie4.setClassification("10");

        var movie5 = new Movies();
        movie5.setImageUrl("https://br.web.img3.acsta.net/c_310_420/medias/nmedia/18/91/54/04/20150812.jpg");
        movie5.setName("Shrek");
        movie5.setDescription("Em um pântano distante vive Shrek (Mike Myers), um ogro solitário que vê, sem mais nem menos, sua vida ser invadida por uma série de personagens de contos de fada, como três ratos cegos, um grande e malvado lobo e ainda três porcos que não têm um lugar onde morar. Todos eles foram expulsos de seus lares pelo maligno Lorde Farquaad (John Lithgow). Determinado a recuperar a tranquilidade de antes, Shrek resolve encontrar Farquaad e com ele faz um acordo: todos os personagens poderão retornar aos seus lares se ele e seu amigo Burro (Eddie Murphy) resgatarem uma bela princesa (Cameron Diaz), que é prisioneira de um dragão. Porém, quando Shrek e o Burro enfim conseguem resgatar a princesa logo eles descobrem que seus problemas estão apenas começando.");
        movie5.setReleaseData(LocalDate.parse("22/06/2001", formatter));
        movie5.setClassification("L");

        var movie6 = new Movies();
        movie6.setImageUrl("https://br.web.img2.acsta.net/c_310_420/pictures/21/02/05/18/17/5028891.jpg");
        movie6.setName("Ip Man: O Mestre do Kung Fu");
        movie6.setDescription("Em Ip Man: O Mestre do Kung Fu, deixando seu passado nas artes marciais para trás, Ip Man (Yu-Hang To) passa a trabalhar como policial. Após sua identidade ficar exposta quando um mafioso já conhecido o reconhece, uma caçada sem fim é iniciada. A situação se complica ainda mais quando a região chinesa onde vive começa a receber ataques de tropas japonesas.");
        movie6.setReleaseData(LocalDate.parse("24/01/2001", formatter));
        movie6.setClassification("14");

        var movie7 = new Movies();
        movie7.setImageUrl("https://br.web.img3.acsta.net/c_310_420/pictures/18/07/18/21/53/1348208.jpg");
        movie7.setName("A Freira");
        movie7.setDescription("Fazendo parte da franquia Invocação do Mal, em A Freira, após uma irmã cometer suicídio em um convento na Romênia, o Vaticano envia um padre atormentado e uma noviça para investigar o ocorrido. Arriscando suas vidas, a fé e até suas almas, os dois descobrem um segredo profano no local, confrontando-se com uma força do mal que toma a forma de uma freira demoníaca e transforma o convento num campo de batalha espiritual.");
        movie7.setReleaseData(LocalDate.parse("06/09/2018", formatter));
        movie7.setClassification("16");

        var movie8 = new Movies();
        movie8.setImageUrl("https://br.web.img2.acsta.net/c_310_420/pictures/15/04/27/19/58/151029.jpg");
        movie8.setName("A Visita");
        movie8.setDescription("Um garoto (Ed Oxenbould) e sua irmã (Olivia DeJonge) são mandados pela mãe (Kathryn Hahn) para visitar seus avós que moram em uma remota fazenda. Não demora muito até que os irmãos descubram que os idosos estão envolvidos com coisas profundamente pertubadoras que colocam a vida dos netos em perigo.");
        movie8.setReleaseData(LocalDate.parse("29/11/2015", formatter));
        movie8.setClassification("16");

        var movie9 = new Movies();
        movie9.setImageUrl("https://br.web.img3.acsta.net/c_310_420/medias/nmedia/18/91/21/06/20134446.jpg");
        movie9.setName("6 Balas");
        movie9.setDescription("Andrew Fayden (Joe Flanigan) é um dos principais lutadores de MMA da atualidade, o que fez com que se tornasse mundialmente famoso. Apesar do sucesso, ele enfrenta uma situação bastante complicada quando sua filha, Monica (Anna-Louise Plowman), é sequestrada e incluída no tráfico de pessoas. Sem saber o que fazer, Andrew busca a ajuda de um mercenário, Samson Gaul (Jean-Claude Van Damme), que conhece bem o ramo do tráfico. Só que Samson ainda está traumatizado devido ao seu último trabalho, que não terminou bem. Decidido a se redimir do erro cometido, ele aceita ajudar Andrew a localizar e salvar sua filha.");
        movie9.setReleaseData(LocalDate.parse("01/05/2013", formatter));
        movie9.setClassification("16");
        movieRepository.saveAll(Arrays.asList(movie1,movie2,movie3,movie4,movie5,movie6,movie7,movie8,movie9));

        var category = new Categories();
        category.setName("artes marciais");

        var category1 = new Categories();
        category1.setName("ação");

        var category2 = new Categories();
        category2.setName("suspense");

        var category3 = new Categories();
        category3.setName("comédia");

        var category4 = new Categories();
        category4.setName("aventura");

        var category5 = new Categories();
        category5.setName("terror");

        var category6 = new Categories();
        category6.setName("drama");

        var category7 = new Categories();
        category7.setName("romance");

        var category8 = new Categories();
        category8.setName("psicologico");

        var category9 = new Categories();
        category9.setName("crime");

        var category10 = new Categories();
        category10.setName("mistério");

        var category11 = new Categories();
        category11.setName("musical");

        var category12 = new Categories();
        category12.setName("fantasia");

        var category13 = new Categories();
        category13.setName("familia");

        var category14 = new Categories();
        category14.setName("Ficção Científica");


        categoryRepository.saveAll(Arrays.asList(category,category1,category2,category3,category4,category5,category6,category7,category8,category9,category10,category11,category12,category13,category14));

        movie9.getCategories().add(category1);
        movie9.getCategories().add(category2);

        movie8.getCategories().add(category5);
        movie8.getCategories().add(category2);

        movie7.getCategories().add(category5);

        movie6.getCategories().add(category1);
        movie6.getCategories().add(category);

        movie5.getCategories().add(category3);
        movie5.getCategories().add(category12);
        movie5.getCategories().add(category13);

        movie4.getCategories().add(category4);
        movie4.getCategories().add(category13);
        movie4.getCategories().add(category12);

        movie3.getCategories().add(category1);
        movie3.getCategories().add(category6);
        movie3.getCategories().add(category13);
        movie3.getCategories().add(category);

        movie2.getCategories().add(category1);
        movie2.getCategories().add(category4);
        movie2.getCategories().add(category14);

        movie1.getCategories().add(category1);
        movie1.getCategories().add(category9);
        movie1.getCategories().add(category4);
        movieRepository.saveAll(Arrays.asList(movie1,movie2,movie3,movie4,movie5,movie6,movie7,movie8,movie9));

    }
}
