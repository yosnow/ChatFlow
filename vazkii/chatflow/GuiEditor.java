package vazkii.chatflow;

import java.awt.Desktop;
import java.net.URI;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiEditor extends GuiScreen {

	GuiTextField nameField, matcherField, replacementField, testField;
	static int index = 0;
	static String test = "";

	@Override
	public void initGui() {
		if(ChatFlow.replacements.isEmpty())
			ChatFlow.replacements.add(new Replacement());

		buttonList.clear();
		buttonList.add(new GuiButton(0, width / 2 - 60, 30, 20, 20,"<"));
		buttonList.add(new GuiButton(1, width / 2 + 40, 30, 20, 20,">"));
		buttonList.add(new GuiButton(2, width / 2 - 90, 30, 20, 20,"X"));
		buttonList.add(new GuiButton(3, width / 2 + 70, 30, 20, 20,"+"));
		buttonList.add(new GuiButton(4, width / 2 - 220, 195, 200, 20,"Regex Help"));
		buttonList.add(new GuiButton(5, width / 2 + 20, 195, 200, 20,"Formatting Help"));

		int border = 60;
		nameField = new GuiTextField(fontRendererObj, border, 70, width - border * 2, 12);
		matcherField = new GuiTextField(fontRendererObj, border, 110, width - border * 2, 12);
		replacementField = new GuiTextField(fontRendererObj, border, 150, width - border * 2, 12);
		testField = new GuiTextField(fontRendererObj, border, 240, width - border * 2, 12);

		nameField.setEnableBackgroundDrawing(false);
		matcherField.setEnableBackgroundDrawing(false);
		replacementField.setEnableBackgroundDrawing(false);
		testField.setEnableBackgroundDrawing(false);

		nameField.setMaxStringLength(9001);
		matcherField.setMaxStringLength(9001);
		replacementField.setMaxStringLength(9001);
		testField.setMaxStringLength(9001);

		updateText();
		testField.setText(test);

		nameField.setFocused(true);

		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch(button.id) {
		case 0 : { // Left
			if(index > 0) {
				index--;
				updateText();
			}
			break;
		}
		case 1 : { // Right
			if(index < ChatFlow.replacements.size() - 1) {
				index++;
				updateText();
			}
			break;
		}
		case 2 : { // Remove
			ChatFlow.replacements.remove(index);
			if(ChatFlow.replacements.size() == 0)
				ChatFlow.replacements.add(new Replacement());

			if(index >= ChatFlow.replacements.size())
				index = ChatFlow.replacements.size() - 1;
			updateText();
			break;
		}
		case 3 : { // Add
			ChatFlow.replacements.add(new Replacement());
			index = ChatFlow.replacements.size() - 1;
			updateText();
			break;
		}
		case 4 : { // Regex Help
			openWebpage("https://www.debuggex.com/cheatsheet/regex/pcre");
			break;
		}
		case 5 : { // Formatting Help
			openWebpage("http://minecraft.gamepedia.com/Formatting_codes");
			break;
		}
		}
	}

	private void openWebpage(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void updateText() {
		Replacement r = ChatFlow.replacements.get(index);
		nameField.setText(r.comment);
		matcherField.setText(r.matcher);
		replacementField.setText(r.replacement);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		super.drawScreen(par1, par2, par3);

		drawField(nameField, "Name:");
		drawField(matcherField, "Matcher:");
		drawField(replacementField, "Replacement:");
		drawField(testField, "Message Test:");

		GL11.glEnable(GL11.GL_BLEND);
		drawCenteredString(fontRendererObj, index + 1 + " / " + ChatFlow.replacements.size(), width / 2, 35, 0xFFFFFF);

		drawCenteredString(fontRendererObj, "Tip: use" + EnumChatFormatting.ITALIC + "^message$" + EnumChatFormatting.RESET + " as the Matcher and an empty Replacement to prevent that message from appearing in chat.", width / 2, replacementField.yPosition + replacementField.height, 0x44FFFFFF);
		drawCenteredString(fontRendererObj, "Use ~ as a replacement for the control character. Use \\~ to write ~.", width / 2, replacementField.yPosition + replacementField.height + 11, 0x44FFFFFF);

		drawString(fontRendererObj, EnumChatFormatting.BOLD + "Message:", testField.xPosition + 10, testField.yPosition + testField.height + 5, 0xFFFFFF);
		drawString(fontRendererObj, EnumChatFormatting.BOLD + "Output:", testField.xPosition + 10, testField.yPosition + testField.height + 30, 0xFFFFFF);

		drawString(fontRendererObj, MessageMeddler.meddleWithMessage(test, true, true), testField.xPosition + 10, testField.yPosition + testField.height + 15, 0xFFFFFF);
		drawString(fontRendererObj, MessageMeddler.meddleWithMessage(test, false, true), testField.xPosition + 10, testField.yPosition + testField.height + 40, 0xFFFFFF);
		GL11.glDisable(GL11.GL_BLEND);
	}

	private void drawField(GuiTextField field, String label) {
		GL11.glEnable(GL11.GL_BLEND);
		drawString(fontRendererObj, label, field.xPosition, field.yPosition - 12, 0xAAFFFFFF);
		drawRect(field.xPosition - 3, field.yPosition - 3, field.xPosition + field.width + 3, field.yPosition + field.height - 1, 0x22000000);
		drawRect(field.xPosition - 2, field.yPosition - 2, field.xPosition + field.width + 2, field.yPosition + field.height - 2, 0x66000000);

		field.drawTextBox();
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		nameField.mouseClicked(par1, par2, par3);
		matcherField.mouseClicked(par1, par2, par3);
		replacementField.mouseClicked(par1, par2, par3);
		testField.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);

		if(par1 == 9) {
			boolean shift = isShiftKeyDown();
			boolean nameFocus = nameField.isFocused();
			boolean matcherFocus = matcherField.isFocused();
			boolean replacementFocus = replacementField.isFocused();
			boolean testFocus = testField.isFocused();

			nameField.setFocused(shift ? matcherFocus : testFocus);
			matcherField.setFocused(shift ? replacementFocus : nameFocus);
			replacementField.setFocused(shift ? testFocus : matcherFocus);
			testField.setFocused(shift ? nameFocus : replacementFocus);
		}

		nameField.textboxKeyTyped(par1, par2);
		matcherField.textboxKeyTyped(par1, par2);
		replacementField.textboxKeyTyped(par1, par2);
		testField.textboxKeyTyped(par1, par2);

		Replacement r = ChatFlow.replacements.get(index);
		r.comment = nameField.getText();
		r.matcher = matcherField.getText();
		r.replacement = replacementField.getText();
		test = testField.getText();
	}

	@Override
	public void onGuiClosed() {
		ChatFlow.saveReplacements();
		Keyboard.enableRepeatEvents(false);
	}

}
