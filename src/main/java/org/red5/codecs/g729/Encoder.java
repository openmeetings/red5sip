package org.red5.codecs.g729;



public class Encoder {

	private CodLD8K encoder = new CodLD8K();

	private PreProc preProc = new PreProc();

	private short[] serial = new short[LD8KConstants.SERIAL_SIZE];

	private int[] prm = new int[LD8KConstants.PRM_SIZE];

	public Encoder() {

		preProc.init_pre_process();
		encoder.init_coder_ld8k();
	}

	// Recebe um array de float de 160 ou 240 Bytes
	// processa de 80 em 80 Bytes e retorna um array de byte de 20 ou 30
	// posi��es
	public void encode(float[] bufferIn, byte[] bufferOut) {

		int inOffset = 0;
		int outOffset = 0;
		int steps = bufferIn.length / LD8KConstants.L_FRAME;

		for (int i = 0; i < steps; i++) {
			byte[] tempBufferOut = new byte[LD8KConstants.L_ENC_FRAME];
			float[] tempBufferIn = new float[LD8KConstants.L_FRAME];

			// Encode bufferIn
			System.arraycopy(bufferIn, inOffset, tempBufferIn, 0, LD8KConstants.L_FRAME);
			tempBufferOut = process(tempBufferIn);

			// Copy encoded data to bufferOut
			System.arraycopy(tempBufferOut, 0, bufferOut, outOffset, LD8KConstants.L_ENC_FRAME);

			inOffset += LD8KConstants.L_FRAME;
			outOffset += LD8KConstants.L_ENC_FRAME;
		}
	}

	/**
	 * Perform G729 encoding
	 * 
	 * @param input
	 *            media
	 * @return compressed media.
	 */
	private byte[] process(float[] media) {

		preProc.pre_process(media, LD8KConstants.L_FRAME);

		encoder.loadSpeech(media);
		encoder.coder_ld8k(prm, 0);

		Bits.prm2bits_ld8k(prm, serial);
		return Bits.toRealBits(serial);
	}
}
