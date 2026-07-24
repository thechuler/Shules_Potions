package net.shule.shulespotions.Screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class EffectCodexScreen extends Screen {
    public EffectCodexScreen() {
        super(Component.translatable("shulespotion.items.effect_codex"));
        effects = ForgeRegistries.MOB_EFFECTS
                .getValues()
                .stream()
                .toList();
    }

    private static final ResourceLocation SCROLL_BACKGROUND =
            ResourceLocation.fromNamespaceAndPath("shulespotions", "textures/gui/effect_codex_screen.png");

    private static final int GUI_WIDTH = 384;
    private static final int GUI_HEIGHT = 240;
    private final List<MobEffect> effects;

    private static final int EFFECT_SIZE = 28;

    private static final int EFFECT_SPACING_X = 34;
    private static final int EFFECT_SPACING_Y = 34;

    private static final int COLUMNS = 4;
    private static final int ROWS = 5;

    private static final int EFFECTS_PER_PAGE = COLUMNS * ROWS * 2;
    private int currentPage = 0;
    private Button nextButton;
    private Button previousButton;
    private final List<EffectButton> effectButtons = new ArrayList<>();

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pGuiGraphics);
        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;
        renderBackgroundTexture(pGuiGraphics, x, y);


        pGuiGraphics.drawCenteredString(
                font,
                "Page " + (currentPage + 1) + "/" + getMaxPages(),
                x + GUI_WIDTH / 2,
                y + GUI_HEIGHT - 18,
                0x5E4A32
        );
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

    }



    private void renderBackgroundTexture(GuiGraphics graphics, int x, int y) {
        graphics.blit(
                SCROLL_BACKGROUND,
                x,
                y,
                0,
                0,
                GUI_WIDTH,
                GUI_HEIGHT,
                GUI_WIDTH,
                GUI_HEIGHT
        );
    }


    private void renderEffects(GuiGraphics graphics, int mouseX, int mouseY, int x, int y) {


        int startIndex =
                currentPage * EFFECTS_PER_PAGE;



        int pageWidth = GUI_WIDTH / 2;


        int gridWidth =
                (COLUMNS - 1) * EFFECT_SPACING_X
                        + EFFECT_SIZE;


        int gridHeight =
                (ROWS - 1) * EFFECT_SPACING_Y
                        + EFFECT_SIZE;


        int leftPageStart =
                x + (pageWidth - gridWidth) / 2;


        int rightPageStart =
                x + pageWidth
                        + (pageWidth - gridWidth) / 2;



        int topStart =
                y + (GUI_HEIGHT - gridHeight) / 2;



        for(int i = 0; i < EFFECTS_PER_PAGE; i++){


            int effectIndex =
                    startIndex + i;


            if(effectIndex >= effects.size())
                break;



            MobEffect effect =
                    effects.get(effectIndex);



            boolean rightPage =
                    i >= COLUMNS * ROWS;



            int localIndex =
                    rightPage
                            ? i - (COLUMNS * ROWS)
                            : i;



            int column =
                    localIndex % COLUMNS;


            int row =
                    localIndex / COLUMNS;



            int iconX =
                    (rightPage ? rightPageStart : leftPageStart)
                            + column * EFFECT_SPACING_X;


            int iconY =
                    topStart
                            + row * EFFECT_SPACING_Y;



            renderEffectIcon(
                    graphics,
                    effect,
                    iconX,
                    iconY,
                    mouseX,
                    mouseY
            );
        }
    }

    private void renderEffectIcon(GuiGraphics graphics, MobEffect effect, int x, int y, int mouseX,
            int mouseY){


        int size = EFFECT_SIZE;


        boolean hovered = mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;

        float scale = 1F;


        if(hovered){
            float time = (System.currentTimeMillis()%1000) /1000F;


            scale = 1F + (float)Math.sin(time * Math.PI * 2) * 0.15F;
        }



        ResourceLocation id = ForgeRegistries.MOB_EFFECTS.getKey(effect);

        if(id == null)
            return;


        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(id.getNamespace(),
                "textures/mob_effect/" + id.getPath() + ".png");



        graphics.pose().pushPose();


        graphics.pose().translate(
                x + size / 2,
                y + size / 2,
                0
        );


        graphics.pose().scale(
                scale,
                scale,
                1
        );


        graphics.blit(
                texture,
                -size / 2,
                -size / 2,
                0,
                0,
                size,
                size,
                size,
                size
        );



        if(hovered){
       //     renderGlow(graphics, -12, -12);
        }

        graphics.pose().popPose();


        if(hovered){
            graphics.renderTooltip(font, Component.translatable(effect.getDescriptionId()), mouseX, mouseY);
        }

    }



    private void renderGlow(GuiGraphics graphics, int x, int y){

        graphics.setColor(1F, 1F, 0.5F, 0.35F);

        graphics.fill(x, y, x+24, y+24, 0xFFFFFFFF);

        graphics.setColor(1F, 1F, 1F, 1F);
    }


    private int getMaxPages() {
        return (effects.size() + EFFECTS_PER_PAGE - 1) / EFFECTS_PER_PAGE;
    }

    @Override
    protected void init() {

        int x = (this.width - GUI_WIDTH) / 2;
        int y = (this.height - GUI_HEIGHT) / 2;

        previousButton = addRenderableWidget(
                Button.builder(Component.literal("<"), b -> previousPage())
                        .bounds(x + 15, y + GUI_HEIGHT - 25, 20, 20)
                        .build()
        );

        nextButton = addRenderableWidget(
                Button.builder(Component.literal(">"), b -> nextPage())
                        .bounds(x + GUI_WIDTH - 35, y + GUI_HEIGHT - 25, 20, 20)
                        .build()
        );

        updateButtons();
        rebuildEffectButtons();
    }


    private void rebuildEffectButtons() {

        for (EffectButton button : effectButtons) {
            removeWidget(button);
        }

        effectButtons.clear();

        int startIndex = currentPage * EFFECTS_PER_PAGE;

        int pageWidth = GUI_WIDTH / 2;

        int gridWidth = (COLUMNS - 1) * EFFECT_SPACING_X + EFFECT_SIZE;

        int gridHeight = (ROWS - 1) * EFFECT_SPACING_Y + EFFECT_SIZE;

        int leftPageStart = (this.width - GUI_WIDTH) / 2 + (pageWidth - gridWidth) / 2;

        int rightPageStart = (this.width - GUI_WIDTH) / 2 + pageWidth + (pageWidth - gridWidth) / 2;

        int topStart = (this.height - GUI_HEIGHT) / 2 + (GUI_HEIGHT - gridHeight) / 2;

        for (int i = 0; i < EFFECTS_PER_PAGE; i++) {

            int effectIndex = startIndex + i;

            if (effectIndex >= effects.size())
                break;

            MobEffect effect = effects.get(effectIndex);

            boolean rightPage = i >= (COLUMNS * ROWS);

            int localIndex = rightPage ? i - (COLUMNS * ROWS) : i;

            int column = localIndex % COLUMNS;
            int row = localIndex / COLUMNS;

            int iconX = (rightPage ? rightPageStart : leftPageStart) + column * EFFECT_SPACING_X;

            int iconY = topStart + row * EFFECT_SPACING_Y;

            EffectButton button = new EffectButton(
                    iconX,
                    iconY,
                    EFFECT_SIZE,
                    effect,
                    b -> Minecraft.getInstance().setScreen(
                            new EffectInfoScreen(
                                    Minecraft.getInstance().screen, effect)
                    ));

            effectButtons.add(button);
            addRenderableWidget(button);
        }
    }

    private void updateButtons() {
        previousButton.active = currentPage > 0;
        nextButton.active = currentPage < getMaxPages() - 1;
    }


    private void nextPage() {
        if (currentPage < getMaxPages() - 1) {
            currentPage++;
            rebuildEffectButtons();
            updateButtons();
        }
    }

    private void previousPage() {
        if (currentPage > 0) {
            currentPage--;
            rebuildEffectButtons();
            updateButtons();
        }
    }
}
