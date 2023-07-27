import java.awt.image.BufferedImage;

public class Texture {
    SpriteSheet as,ps;
    private BufferedImage apple_sheet = null;
    private BufferedImage player_sheet = null;

    public BufferedImage[] player = new BufferedImage[19];
    public BufferedImage[] apple = new BufferedImage[1];
    public Texture(){

        BufferedImageLoader loader = new BufferedImageLoader();
        try{
            apple_sheet = loader.loadImage("/Snake.png");
            player_sheet = loader.loadImage("/Snake.png");
        }catch (Exception e) {
            e.printStackTrace();
        }

        as = new SpriteSheet(apple_sheet);
        ps = new SpriteSheet(player_sheet);
        
        getTextures();
    }

    private void getTextures() {
        player[0] = ps.grabImage(4,2,20,20); //head right
        player[1] = ps.grabImage(5,1,20,20); //head left
        player[2] = ps.grabImage(4,1,20,20); //head up
        player[3] = ps.grabImage(5,2,20,20); //head down

        player[4] = ps.grabImage(2,1,20,20); //body horizontally
        player[6] = ps.grabImage(3,2,20,20); //body vertically

        player[7] = ps.grabImage(3,1,20,20); //body horizontally turn down - right
        player[8] = ps.grabImage(3,3,20,20); //body horizontally turn up   - right
        player[9] = ps.grabImage(1,1,20,20); //body horizontally turn down - left
        player[10] =ps.grabImage(1,2,20,20); //body horizontally turn up   - left

        player[11] = ps.grabImage(1,1,20,20); //body vertically  turn up   - right
        player[12] = ps.grabImage(3,1,20,20); //body vertically  turn up   - left
        player[13] = ps.grabImage(1,2,20,20); //body vertically  turn down - right
        player[14] = ps.grabImage(3,3,20,20); //body vertically  turn down - left

        player[15] = ps.grabImage(3,2,20,20); //tail up
        player[16] = ps.grabImage(3,2,20,20); //tail right
        player[17] = ps.grabImage(3,2,20,20); //tail left
        player[18] = ps.grabImage(3,2,20,20); //tail down

        apple[0] = as.grabImage(1,4,20,20); //apple

    }
}
