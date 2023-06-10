package org.example.packets.encoding;

public interface Codec<CodecableEssence> extends
        EncryptionProvider<CodecableEssence>,
        DecryptionProvider<CodecableEssence> {

}
