package Tools;
import Structures.Musica;

import java.util.Random;

public class MockData {
        static String[] names = { "Dance Monkey", "Blinding Lights", "Roses", "Don't Start Now", "Watermelon Sugar",
                        "Savage Love", "Rockstar", "Whats Poppin", "Blueberry Faygo", "The Box",
                        "Rain On Me", "Say So", "Intentions", "Stuck With U", "Toosie Slide",
                        "Break My Heart", "Adore You", "Supalonely", "Savage", "Circles",
                        "Falling", "Sunday Best", "Death Bed", "Go Crazy", "Mood", "WAP",
                        "Levitating", "Holy", "Laugh Now Cry Later", "Before You Go",
                        "Roses", "Life Is Good", "The Bones", "One Margarita",
                        "One Big Country Song", "More Than My Hometown", "Cool Again",
                        "I Hope", "Got What I Got", "Happy Anywhere", "One Night Standards",
                        "Die From A Broken Heart", "After A Few", "Chasin' You",
                        "I Hope You're Happy Now", "Nobody But You", "One Of Them",
                        "In My Feelings", "Old Town Road", "Shape of You", "Despacito",
                        "Uptown Funk", "Closer", "Believer", "Havana", "Someone You Loved",
                        "Girls Like You", "Thank U, Next", "Sicko Mode", "High Hopes",
                        "Sunflower", "7 Rings", "Without Me", "Bad Guy", "Wow.",
                        "Lucid Dreams", "Better Now", "Happier", "No Tears Left To Cry",
                        "Truth Hurts", "Eastside", "Better", "Falling", "Wow.",
                        "Sucker", "Senorita", "Suge", "Talk", "If I Can't Have You",
                        "Panini", "Money In The Grave", "Mia", "Goodbyes",
                        "Sweet But Psycho", "Break Up With Your Girlfriend, I'm Bored",
                        "God's Plan", "Shallow", "Better Now", "Breathin",
                        "Walk Me Home", "I Don't Care", "Happier", "Better",
                        "Sicko Mode", "Truth Hurts", "Without Me", "Sunflower",
                        "Ran$om", "Sweet But Psycho", "Suge", "Going Bad",
                        "7 Rings", "Dancing With A Stranger", "Middle Child", "Thank U, Next",
                        "Wow.", "Bad Guy", "Con Calma", "Money", "Kill This Love",
                        "You Need To Calm Down", "Dancing With A Stranger", "SOS",
                        "Earth", "Pure Water", "Old Town Road", "Going Bad",
                        "Swervin", "Worth It", "Bury A Friend", "Here With Me",
                        "Please Me", "Sweet But Psycho", "Sunflower", "Wow.",
                        "Talk", "Look Back At It", "Close To Me", "Envy Me",
                        "Pure Water", "Please Me", "Better", "Going Bad",
                        "Wake Up In The Sky", "Thotiana", "Going Bad", "Girls Need Love",
                        "Swervin", "Better", "Thotiana", "Con Calma",
                        "Envy Me", "Better", "Clout", "Middle Child",
                        "Here With Me", "Racks In The Middle", "Going Bad",
                        "Please Me", "Clout", "Sicko Mode", "A Lot",
                        "Pure Water", "Wow.", "Close Friends", "Thotiana",
                        "Going Bad", "Better", "Envy Me", "Please Me",
                        "Middle Child", "Pure Water", "Thotiana", "Con Calma",
                        "Close Friends", "Middle Child", "Close Friends", "Wow.",
                        "Clout", "Close Friends", "Con Calma", "Pure Water",
                        "Going Bad", "Clout", "Pure Water", "Close Friends",
                        "Close Friends", "Con Calma", "Close Friends",
                        "Please Me", "Pure Water", "Wow.", "Middle Child",
                        "Clout", "Pure Water", "Con Calma", "Close Friends",
                        "Wow.", "Please Me", "Thotiana", "Con Calma",
                        "Pure Water", "Wow.", "Close Friends", "Middle Child",
                        "Please Me", "Clout", "Close Friends", "Thotiana",
                        "Wow.", "Close Friends", "Pure Water", "Middle Child",
                        "Clout", "Wow.", "Please Me", "Thotiana",
                        "Middle Child", "Con Calma", "Close Friends", "Pure Water",
                        "Wow.", "Thotiana", "Please Me", "Middle Child",
                        "Clout", "Con Calma", "Close Friends", "Wow.",
                        "Pure Water", "Please Me", "Thotiana", "Middle Child",
                        "Wow.", "Clout", "Close Friends", "Con Calma",
                        "Pure Water", "Please Me", "Thotiana", "Middle Child",
                        "Wow.", "Clout", "Close Friends", "Con Calma",
                        "Pure Water", "Please Me", "Thotiana", "Middle Child",
                        "Wow.", "Clout", "Close Friends", "Con Calma" };
        static String data = "2021-06-01";
        static String[] artists = { "João", "Maria", "José", "Ana", "Carlos", "Pedro", "Paula",
                        "Luana", "Mariana", "Joaquim", "Lucas", "Gabriel", "Rafael",
                        "Fernanda", "Juliana", "Larissa", "Leticia", "Amanda", "Bruno",
                        "Gustavo", "Ricardo", "Rodrigo", "Rafaela", "Isabela", "Laura",
                        "Felipe", "Daniela", "Roberto", "Eduardo", "Fernando", "Vanessa",
                        "Camila", "Vinicius", "Raquel", "Thiago", "Marcelo", "Sandra",
                        "Tatiane", "Diego", "Aline", "Patricia", "Leonardo", "Fabio",
                        "Renata", "Vitor", "Sonia", "Ramon", "Cristina", "Erika", "Silvio",
                        "Leandro", "Luiz", "Marcela", "Sergio", "Flavia", "Jessica",
                        "Robson", "Paulo", "Luisa", "Andrea", "Monica", "Marcos", "Sabrina",
                        "Carolina", "Michelle", "Sara", "Julio", "Mauricio", "Lorena",
                        "Anderson", "Fabiana", "Vivian", "Talita", "Renan", "Adriana",
                        "Rita", "Luciana", "Alice", "Jonas", "Natalia", "Carla", "Wagner",
                        "Jaqueline", "Alexandre", "Juliane", "Flavio", "Helena", "Samuel",
                        "Cleber", "Rosana", "Cesar", "Marta", "Junior", "Debora", "Elias",
        };
        static String[] sobrenomeArtistas = { "Silva", "Santos", "Oliveira", "Pereira", "Almeida", "Lima", "Costa",
                        "Ferreira", "Rodrigues", "Gomes", "Martins", "Carvalho", "Rocha",
                        "Rezende", "Barros", "Cardoso", "Nunes", "Cavalcante", "Mendes",
                        "Araujo", "Gonçalves", "Ribeiro", "Alves", "Monteiro", "Melo",
                        "Sousa", "Santana", "Andrade", "Teixeira", "Moraes", "Lopes",
                        "Fogaça", "Correia", "Campos", "Braga", "Pinto", "Macedo",
                        "Lins", "Dias", "Castro", "Freitas", "Barbosa", "Nogueira",
                        "Machado", "Peixoto", "Nascimento", "Bezerra", "Marques",
                        "Fernandes", "Viana", "Vieira", "Oliveira", "Lira",
                        "Farias", "Duarte", "Lacerda", "Assis", "Ferreira",
                        "Tavares", "Vargas", "Medeiros", "Morais", "Domingues",
                        "Guimarães", "Gusmão", "Dutra", "Caldeira", "Pacheco",
                        "Aguiar", "Abreu", "Silveira", "Fonseca", "Aragão",
                        "Godoy", "Galvão", "Chaves", "Fagundes", "Cardoso",
                        "Salles", "Cunha", "Barros", "Garcia", "Muniz",
                        "Diniz", "Couto", "Neves", "Brito", "Lima",
                        "Bastos", "Rocha", "Magalhães", "Sales", "Bragança",
                        "Queiroz", "Azevedo" };
        static int[] popularity = { 80, 90, 85, 70, 60, 75, 65, 95, 100, 55, 2000, 1000, 3000, 4000, 5000, 6000, 7000,
                        8000,
                        9000, 10000 };
        static String[] genres = { "Pop", "Rock", "Hip-Hop", "Rap", "Reggae", "Sertanejo",
                        "Funk", "Samba", "Forró", "Jazz", "Blues", "Clássica",
                        "Eletrônica", "Soul", "R&B", "Country", "Disco", "Reggaeton",
                        "Indie", "Folk", "EDM", "Metal", "Punk", "Alternative",
                        "Gospel", "Classical", "Techno", "House", "Ambient",
                        "Chill", "Trap", "Dubstep", "Grime", "Dancehall",
                        "Jungle", "Drum and Bass", "Breakbeat", "Downtempo",
                        "Hardstyle", "Trance", "Garage", "Big Room", "Progressive House",
                        "Synthwave", "Hardcore", "Acid House", "IDM", "Experimental",
                        "Industrial", "Glitch", "Electroswing", "Tropical House", "Nu Disco",
                        "Chiptune", "Post-Rock", "Shoegaze", "Dream Pop", "R&B",
                        "Neo Soul", "Funk", "Disco", "Grunge", "Hard Rock",
                        "Soft Rock", "Progressive Rock", "Folk Rock", "Psychedelic Rock",
                        "Indie Rock", "Britpop", "Death Metal", "Black Metal", "Thrash Metal",
                        "Glam Metal", "Power Metal", "Doom Metal", "Gothic Metal",
                        "Symphonic Metal", "Melodic Metal" };
        static float[] danceability = { 0.7f, 0.8f, 0.75f, 0.6f, 0.65f, 0.85f, 0.9f, 0.95f, 0.55f, 0.5f, 0.4f, 0.3f,
                        0.2f,
                        0.1f, 0.05f, 0.15f, 0.25f, 0.35f, 0.45f, 0.55f };
        static String[] hashes = { "7fvUMiyapMsRRxr07cU8Ef", "1mC2UjWt25Oixtqu7C6suL", "3TjLsDgL0bTbSQIF6M5Ki8",
                        "4sqABRRGU7CzcHXCyxUzFw" };

        Musica Mock;
        // Método para gerar dados aleatórios
        public Musica generateRandomData() {
                Random rand = new Random();
                Mock = new Musica();
                int randomIndex;

                // Gerar dados aleatórios
                randomIndex = rand.nextInt(names.length);
                String randomName = names[randomIndex];

                randomIndex = rand.nextInt(artists.length);
                String randomArtist = artists[randomIndex];

                randomIndex = rand.nextInt(sobrenomeArtistas.length);
                randomArtist += (" " + sobrenomeArtistas[randomIndex]);

                randomIndex = rand.nextInt(popularity.length);
                int randomPopularity = popularity[randomIndex];

                randomIndex = rand.nextInt(genres.length);
                String randomGenre = genres[randomIndex] + " ; ";
                randomIndex = rand.nextInt(genres.length);
                randomGenre += genres[randomIndex];

                randomIndex = rand.nextInt(danceability.length);
                float randomDanceability = danceability[randomIndex];

                randomIndex = rand.nextInt(hashes.length);
                String randomHash = hashes[randomIndex];

                return new Musica(randomName, randomArtist, randomPopularity, data, randomGenre, randomDanceability,
                                randomHash);
        }

}
