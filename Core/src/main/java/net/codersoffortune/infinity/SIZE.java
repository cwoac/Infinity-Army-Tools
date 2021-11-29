package net.codersoffortune.infinity;

import net.codersoffortune.infinity.db.Database;

public enum SIZE {
    /* A little note on S0. Technically, this is SX, which is the 3mm high version of the silhouette.
           However, the only units which have SX on their profile are the seed embryos, which are all Sx2,
           so S0 is S2 scaled to 3mm high.
     */
    SX("0.075","http://cloud-3.steamusercontent.com/ugc/925928630318913338/E7DA6223E51BEF6492FBDDDD7C2074898E601180/","http://cloud-3.steamusercontent.com/ugc/1744573743622198051/6E77F14FEBB2339E041E526394205FF61110D508/","http://cloud-3.steamusercontent.com/ugc/1762566040434517424/7BD92CCD59122AA034293F6D291C3B4227CF46CC/","http://cloud-3.steamusercontent.com/ugc/1744573743622198051/6E77F14FEBB2339E041E526394205FF61110D508/"),
    S1("1.0","http://cloud-3.steamusercontent.com/ugc/925928630318913338/E7DA6223E51BEF6492FBDDDD7C2074898E601180/","http://cloud-3.steamusercontent.com/ugc/1744573743622198051/6E77F14FEBB2339E041E526394205FF61110D508/","http://cloud-3.steamusercontent.com/ugc/1762566040434517729/16AB5E3A7C708780DD20C78B550CFEB920C689AD/","http://cloud-3.steamusercontent.com/ugc/1744573743622198051/6E77F14FEBB2339E041E526394205FF61110D508/"),
    S2("1.0","http://cloud-3.steamusercontent.com/ugc/925928630318913338/E7DA6223E51BEF6492FBDDDD7C2074898E601180/","http://cloud-3.steamusercontent.com/ugc/1744573743622198051/6E77F14FEBB2339E041E526394205FF61110D508/","http://cloud-3.steamusercontent.com/ugc/1762566040434517424/7BD92CCD59122AA034293F6D291C3B4227CF46CC/","http://cloud-3.steamusercontent.com/ugc/1744573743622198051/6E77F14FEBB2339E041E526394205FF61110D508/"),
    S3("1.0","http://cloud-3.steamusercontent.com/ugc/925928630319000946/59207F2FCCEED4402B0E7406A59BFC34D1B1C687/","http://cloud-3.steamusercontent.com/ugc/1744573743622229157/C0F17D5025E5C46B5F0FD469A16CC393C83D1E0E/","http://cloud-3.steamusercontent.com/ugc/1762566040434516925/35B6115843155E46391551B0AC37D029EF9F3C63/","http://cloud-3.steamusercontent.com/ugc/1744573743622229157/C0F17D5025E5C46B5F0FD469A16CC393C83D1E0E/"),
    S4("1.0", "http://cloud-3.steamusercontent.com/ugc/925928630318994134/5838E51349A25E681B92707DB5A0D10812E987AF/","http://cloud-3.steamusercontent.com/ugc/1744573743622241966/3EE710715ED1F445642E1BCB170EE88125B73B59/","http://cloud-3.steamusercontent.com/ugc/1762566040434514906/E2874F5BC3CFDCCCF8A6B6D6D71B12437809FC48/","http://cloud-3.steamusercontent.com/ugc/1744573743622241966/3EE710715ED1F445642E1BCB170EE88125B73B59/"),
    S5("1.0", "http://cloud-3.steamusercontent.com/ugc/925928630319000946/59207F2FCCEED4402B0E7406A59BFC34D1B1C687/","http://cloud-3.steamusercontent.com/ugc/1744573743622229157/C0F17D5025E5C46B5F0FD469A16CC393C83D1E0E/","http://cloud-3.steamusercontent.com/ugc/1762566040434514388/5CAEBDC5BA9DE7ECA334765737E1D10E2F922326/","http://cloud-3.steamusercontent.com/ugc/1744573743622229157/C0F17D5025E5C46B5F0FD469A16CC393C83D1E0E/"),
    S6("1.0", "http://cloud-3.steamusercontent.com/ugc/925928630319000946/59207F2FCCEED4402B0E7406A59BFC34D1B1C687/","http://cloud-3.steamusercontent.com/ugc/1744573743622229157/C0F17D5025E5C46B5F0FD469A16CC393C83D1E0E/","http://cloud-3.steamusercontent.com/ugc/1762566040434513921/CCA4B3F4285094CCD598A2F50B6947786900EBB3/","http://cloud-3.steamusercontent.com/ugc/1744573743622229157/C0F17D5025E5C46B5F0FD469A16CC393C83D1E0E/"),
    S7("1.0", "http://cloud-3.steamusercontent.com/ugc/925928630318994134/5838E51349A25E681B92707DB5A0D10812E987AF/","http://cloud-3.steamusercontent.com/ugc/1744573743622241966/3EE710715ED1F445642E1BCB170EE88125B73B59/","http://cloud-3.steamusercontent.com/ugc/1762566040434513546/34EE1AF3873E76E19912EDE95F2059647D9AB1D9/","http://cloud-3.steamusercontent.com/ugc/1744573743622241966/3EE710715ED1F445642E1BCB170EE88125B73B59/"),
    S8("1.0", "http://cloud-3.steamusercontent.com/ugc/866241932578455959/CAD005E96ED31C2403B780E33C2BFD7FCBC234A2/","http://cloud-3.steamusercontent.com/ugc/1744573743622253962/45A01B760303817D7C3D39F766C79F8653CC07D7/","http://cloud-3.steamusercontent.com/ugc/1762566040434513066/6E1BCD64A56CEEC1169331EE182DA5FF5EA7BE5E/","http://cloud-3.steamusercontent.com/ugc/1744573743622253962/45A01B760303817D7C3D39F766C79F8653CC07D7/");


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
