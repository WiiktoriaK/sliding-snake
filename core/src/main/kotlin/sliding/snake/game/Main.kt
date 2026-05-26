package sliding.snake.game

import com.badlogic.gdx.Game
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import sliding.snake.game.screens.MenuScreen
import sliding.snake.game.screens.PauseScreen


class Main(val stats: StatsRepository) : Game(){

    lateinit var batch: SpriteBatch
    lateinit var assets: AssetManager
    lateinit var shapeRenderer: ShapeRenderer
    lateinit var backgroundMusic: Music
    var musicVolume = 0.5f
    var soundsVolume = 0.5f


    override fun create() {
        batch = SpriteBatch()
        assets = AssetManager()
        shapeRenderer = ShapeRenderer()

        val resolver = InternalFileHandleResolver()
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))

        loadAssets()

        assets.finishLoading()
        backgroundMusic = assets.get(Config.BACKGROUND_SOUND, Music::class.java)
        backgroundMusic.isLooping = true
        backgroundMusic.play()


        this.setScreen(MenuScreen(this))
    }

    private fun loadAssets(){
        // pictures
        assets.load(Config.TEX_START_BUTTON, Texture::class.java)
        assets.load(Config.TEX_PAUSE_BUTTON, Texture::class.java)
        assets.load(Config.TEX_RESUME_BUTTON, Texture::class.java)
        assets.load(Config.TEX_MENU_BUTTON, Texture::class.java)
        assets.load(Config.TEX_RETRY_BUTTON, Texture::class.java)
        assets.load(Config.TEX_PROFILE_BUTTON, Texture::class.java)
        assets.load(Config.TEX_RESET_BUTTON, Texture::class.java)
        assets.load(Config.TEX_SETTINGS_BUTTON, Texture::class.java)
        assets.load(Config.TEX_SNAKE, Texture::class.java)
        assets.load(Config.TEX_FENCE, Texture::class.java)
        assets.load(Config.TEX_FRUIT, Texture::class.java)
        assets.load(Config.TEX_MYSTERY_BOX, Texture::class.java)

        // sounds
        assets.load(Config.BACKGROUND_SOUND, Music::class.java)
        assets.load(Config.DEATH_SOUND, Sound::class.java)
        assets.load(Config.BOX_SOUND, Sound::class.java)
        assets.load(Config.BITE_SOUND, Sound::class.java)

        // fonts
        val mainFontParams = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        mainFontParams.fontFileName = Config.MAIN_PIXEL_FONT
        mainFontParams.fontParameters.size = 24
        mainFontParams.fontParameters.minFilter = Texture.TextureFilter.Nearest
        mainFontParams.fontParameters.magFilter = Texture.TextureFilter.Nearest
        mainFontParams.fontParameters.genMipMaps = false

        val bigFontParams = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        bigFontParams.fontFileName = Config.BIG_PIXEL_FONT
        bigFontParams.fontParameters.size = 100
        bigFontParams.fontParameters.minFilter = Texture.TextureFilter.Nearest
        bigFontParams.fontParameters.magFilter = Texture.TextureFilter.Nearest
        bigFontParams.fontParameters.genMipMaps = false

        assets.load(Config.MAIN_PIXEL_FONT, BitmapFont::class.java, mainFontParams)
        assets.load(Config.BIG_PIXEL_FONT, BitmapFont::class.java, bigFontParams)

    }

    override fun setScreen(screen: Screen?) {
        if (screen !is PauseScreen) {
            this.screen?.dispose()
        }
        super.setScreen(screen)
    }

    override fun dispose() {
        batch.dispose()
        assets.dispose()
        shapeRenderer.dispose()
    }


}
