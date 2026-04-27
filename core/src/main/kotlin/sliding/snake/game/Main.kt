package sliding.snake.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import org.w3c.dom.Text
import sliding.snake.game.screens.MenuScreen
import sliding.snake.game.screens.PauseScreen


class Main : Game(){

    lateinit var batch: SpriteBatch
    lateinit var assets: AssetManager


    override fun create() {
        batch = SpriteBatch()
        assets = AssetManager()

        //
        val resolver = InternalFileHandleResolver()
        assets.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assets.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))

        loadAssets()

        assets.finishLoading()

        this.setScreen(MenuScreen(this))
    }

    private fun loadAssets(){
        // pictures
        assets.load(Config.TEX_START_BUTTON, Texture::class.java)
        assets.load(Config.TEX_PAUSE_BUTTON, Texture::class.java)
        assets.load(Config.TEX_RESUME_BUTTON, Texture::class.java)
        assets.load(Config.TEX_MENU_BUTTON, Texture::class.java)
        assets.load(Config.TEX_RETRY_BUTTON, Texture::class.java)
        assets.load(Config.TEX_SNAKE, Texture::class.java)
        assets.load(Config.TEX_FENCE, Texture::class.java)
        assets.load(Config.TEX_FRUIT, Texture::class.java)
        assets.load(Config.TEX_MYSTERY_BOX, Texture::class.java)

        // fonts
        val fontParams = FreetypeFontLoader.FreeTypeFontLoaderParameter()
        fontParams.fontFileName = Config.MAIN_PIXEL_FONT
        fontParams.fontParameters.size = 24

        fontParams.fontParameters.minFilter = Texture.TextureFilter.Nearest
        fontParams.fontParameters.magFilter = Texture.TextureFilter.Nearest
        fontParams.fontParameters.genMipMaps = false

        assets.load(Config.MAIN_PIXEL_FONT, BitmapFont::class.java, fontParams)

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
    }


}
