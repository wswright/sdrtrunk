/*******************************************************************************
 *     SDR Trunk 
 *     Copyright (C) 2014,2015 Dennis Sheirer
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package module.decode.nbfm;

import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;
import module.decode.Decoder;
import module.decode.state.ChangedAttribute;
import module.decode.state.DecoderPanel;
import settings.ColorSetting;
import settings.Setting;
import settings.SettingsManager;
import settings.ColorSetting.ColorSettingName;

public class NBFMDecoderPanel extends DecoderPanel
{
    private static final long serialVersionUID = 1L;

    private JLabel mProtocol = new JLabel( "NBFM" );
	
	public NBFMDecoderPanel( SettingsManager settingsManager, Decoder decoder )
	{
		super( settingsManager, decoder );
		
		init();
	}
	
	protected void init()
	{
		super.init();

    	setLayout( new MigLayout( "insets 0 0 0 0", "[grow,fill]", "[]0[]0[]") );
		
		mProtocol.setFont( mFontDecoder );
		mProtocol.setForeground( mColorLabelDecoder );

		add( mProtocol, "wrap" );
	}

	@Override
	public void receive( ChangedAttribute t )
	{
		/* Not implemented */
	}
	
	@Override
    public void settingChanged( Setting setting )
    {
		super.settingChanged( setting );
		
		if( setting instanceof ColorSetting )
		{
			ColorSetting color = (ColorSetting)setting;

			if( color.getColorSettingName() == 
					ColorSettingName.CHANNEL_STATE_LABEL_DECODER )
			{
				mProtocol.setForeground( color.getColor() );
			}
		}
    }
}