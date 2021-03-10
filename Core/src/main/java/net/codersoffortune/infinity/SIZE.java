package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;

public enum SIZE {
    /* A little note on S0. Technically, this is SX, which is the 3mm high version of the silhouette.
           However, the only units which have SX on their profile are the seed embryos, which are all Sx2,
           so S0 is S2 scaled to 3mm high.
     */
    SX("0.075","http://cloud-3.steamusercontent.com/ugc/925928630318913338/E7DA6223E51BEF6492FBDDDD7C2074898E601180/","http://cloud-3.steamusercontent.com/ugc/1012689924693058630/D75278D5B13CD7E5C35A47F605461D582CD8566C/","http://cloud-3.steamusercontent.com/ugc/1762566040434517424/7BD92CCD59122AA034293F6D291C3B4227CF46CC/","http://cloud-3.steamusercontent.com/ugc/1012689924693138917/E38A0C14F29D566E6BDBD3158410F7FBB5996ABB/"),
    S1("1.0","http://cloud-3.steamusercontent.com/ugc/925928630318913338/E7DA6223E51BEF6492FBDDDD7C2074898E601180/","http://cloud-3.steamusercontent.com/ugc/1012689924693058630/D75278D5B13CD7E5C35A47F605461D582CD8566C/","http://cloud-3.steamusercontent.com/ugc/1762566040434517729/16AB5E3A7C708780DD20C78B550CFEB920C689AD/","http://cloud-3.steamusercontent.com/ugc/1012689924693133830/24D30F0275DCE70611D7EAA9298DBE02405E36E5/"),
    S2("1.0","http://cloud-3.steamusercontent.com/ugc/925928630318913338/E7DA6223E51BEF6492FBDDDD7C2074898E601180/","http://cloud-3.steamusercontent.com/ugc/1012689924693058630/D75278D5B13CD7E5C35A47F605461D582CD8566C/","http://cloud-3.steamusercontent.com/ugc/1762566040434517424/7BD92CCD59122AA034293F6D291C3B4227CF46CC/","http://cloud-3.steamusercontent.com/ugc/1012689924693138917/E38A0C14F29D566E6BDBD3158410F7FBB5996ABB/"),
    S3("1.0","http://cloud-3.steamusercontent.com/ugc/925928630319000946/59207F2FCCEED4402B0E7406A59BFC34D1B1C687/","http://cloud-3.steamusercontent.com/ugc/1012689924693058973/F7E8C214C78B0CC828710C7097D7F64DA6C0F0F7/","http://cloud-3.steamusercontent.com/ugc/1762566040434516925/35B6115843155E46391551B0AC37D029EF9F3C63/","http://cloud-3.steamusercontent.com/ugc/1012689924693139824/21CD80D311A1DBEDC440F18367ACFA41039C0315/"),
    S4("1.0", "http://cloud-3.steamusercontent.com/ugc/925928630318994134/5838E51349A25E681B92707DB5A0D10812E987AF/","http://cloud-3.steamusercontent.com/ugc/1012689924693072594/95AA62901F9AFA11B82D170B8A168EDB69D98207/","http://cloud-3.steamusercontent.com/ugc/1762566040434514906/E2874F5BC3CFDCCCF8A6B6D6D71B12437809FC48/","http://cloud-3.steamusercontent.com/ugc/1012689924693141294/D9252565AD8E4B034E0E1E7BB1664ADF00665F9E/"),
    S5("1.0", "http://cloud-3.steamusercontent.com/ugc/925928630319000946/59207F2FCCEED4402B0E7406A59BFC34D1B1C687/","http://cloud-3.steamusercontent.com/ugc/1012689924693058973/F7E8C214C78B0CC828710C7097D7F64DA6C0F0F7/","http://cloud-3.steamusercontent.com/ugc/1762566040434514388/5CAEBDC5BA9DE7ECA334765737E1D10E2F922326/","http://cloud-3.steamusercontent.com/ugc/1012689924693142338/C43BA6E5D53873714D2B800E79516E0672FA42AE/"),
    S6("1.0", "http://cloud-3.steamusercontent.com/ugc/925928630319000946/59207F2FCCEED4402B0E7406A59BFC34D1B1C687/","http://cloud-3.steamusercontent.com/ugc/1012689924693058973/F7E8C214C78B0CC828710C7097D7F64DA6C0F0F7/","http://cloud-3.steamusercontent.com/ugc/1762566040434513921/CCA4B3F4285094CCD598A2F50B6947786900EBB3/","http://cloud-3.steamusercontent.com/ugc/1012689924693143105/A3C825A3B0AEFFC8D27DB48BF41B0A08F1882523/"),
    S7("1.0", "http://cloud-3.steamusercontent.com/ugc/925928630318994134/5838E51349A25E681B92707DB5A0D10812E987AF/","http://cloud-3.steamusercontent.com/ugc/1012689924693072594/95AA62901F9AFA11B82D170B8A168EDB69D98207/","http://cloud-3.steamusercontent.com/ugc/1762566040434513546/34EE1AF3873E76E19912EDE95F2059647D9AB1D9/","http://cloud-3.steamusercontent.com/ugc/1012689924693143903/51B897D6E09EDE8E7B25592D51D7EC563D867C47/"),
    S8("1.0", "http://cloud-3.steamusercontent.com/ugc/866241932578455959/CAD005E96ED31C2403B780E33C2BFD7FCBC234A2/","http://cloud-3.steamusercontent.com/ugc/866241932578456453/38E50EE9F0B2E0DC4036523C2232D3CA6CA57BC9/","http://cloud-3.steamusercontent.com/ugc/1762566040434513066/6E1BCD64A56CEEC1169331EE182DA5FF5EA7BE5E/","http://cloud-3.steamusercontent.com/ugc/1012689924693144429/6E33EAD1463408B86BCCF1C9D7D37CB473E9C971/");


    private final String scale;
    private final String modelMesh;
    private final String modelCollider;
    private final String silhouetteMesh;
    private final String silhouetteCollider;

    SIZE(String scale, String modelMesh, String modelCollider, String silhouetteMesh, String silhouetteCollider) {
        this.scale = scale;
        this.modelMesh = modelMesh;
        this.modelCollider = modelCollider;
        this.silhouetteMesh = silhouetteMesh;
        this.silhouetteCollider = silhouetteCollider;
    }

    public String getModelCustomMesh(final String baseImage) {
        return String.format(
          "{\n" +
                  "\"MeshURL\": \"%s\",\n" +
                  "\"DiffuseURL\": \"%s\",\n" +
                  "\"NormalURL\": \"\",\n" +
                  "\"ColliderURL\": \"%s\",\n" +
                  "\"Convex\": true,\n" +
                  "\"MaterialIndex\": 3,\n" +
                  "\"TypeIndex\": 1,\n" +
                  "\"CastShadows\": true\n" +
                  "}",
                modelMesh,
                baseImage,
                modelCollider
        );
    }

    public String getSilhouetteTemplate() {
        return String.format(Database.getMeshTemplate(),
                scale,
                "%s", // name
                "%s", // colour
                silhouetteMesh,
                "%s", // side decal
                silhouetteCollider,
                "%s", // addon
                "%s" // top decal
        );
    }

    public static SIZE get(int value) throws IndexOutOfBoundsException {
        switch (value) {
            case 0: return SX;
            case 1: return S1;
            case 2: return S2;
            case 3: return S3;
            case 4: return S4;
            case 5: return S5;
            case 6: return S6;
            case 7: return S7;
            case 8: return S8;
            default:
                throw new IndexOutOfBoundsException(String.format("Unknown size %s", value));
        }
    }
}
