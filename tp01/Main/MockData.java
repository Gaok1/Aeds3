import java.util.Random;

public class MockData {
    static String[] names = { "Dance Monkey", "Blinding Lights", "Roses", "Don't Start Now", "Watermelon Sugar",
            "Savage Love", "Rockstar", "Whats Poppin", "Blueberry Faygo", "The Box" };
    static String data = "2021-06-01";
    static String[] artists = { "João", "Maria", "José", "Ana", "Carlos", "Pedro", "Paula", "Luana", "Mariana",
            "Joaquim" };
    static String[] sobrenomeArtistas = { "Silva", "Santos", "Oliveira", "Pereira", "Almeida", "Lima", "Costa",
            "Ferreira", "Rodrigues", "Gomes" };
    static int[] popularity = { 80, 90, 85, 70, 60, 75, 65, 95, 100, 55, 2000, 1000, 3000, 4000, 5000, 6000, 7000, 8000,
            9000, 10000 };
    static String[] genres = { "Pop", "Rock", "Hip-Hop", "Rap", "Reggae", "Sertanejo", "Funk", "Samba", "Forró" };
    static float[] danceability = { 0.7f, 0.8f, 0.75f, 0.6f, 0.65f, 0.85f, 0.9f, 0.95f, 0.55f, 0.5f, 0.4f, 0.3f, 0.2f,
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

        return new Musica(randomName, randomArtist, randomPopularity, data, randomGenre, randomDanceability, randomHash);
    }

    
}
 