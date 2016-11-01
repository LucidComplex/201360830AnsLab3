/**
 * Created by tan on 10/31/16.
 */
public class Scale {
    private String name;
    private float[] weights;

    public Scale(String name) {
        this.name = name;
        fillWeights();
    }

    public float getWeight(char protein) {
        int offset = 0;
        if (protein >= 'C' && protein <= 'I') {
            offset = 1;
        } else if (protein >= 'K' && protein <= 'N') {
            offset = 2;
        } else if (protein >= 'P' && protein <= 'T') {
            offset = 3;
        } else if (protein >= 'V' && protein <= 'W') {
            offset = 4;
        } else if (protein == 'Y') {
            offset = 5;
        }
        return weights[protein - 'A' - offset];
    }

    private void fillWeights() {
        weights = new float[26];
        switch (name) {
            case "Kyte-Doolittle":
                weights = new float[] {
                        1.8f, 2.5f, -3.5f, -3.5f, 2.8f, -0.4f,
                        -3.2f, 4.5f, -3.9f, 3.8f, 1.9f, -3.5f,
                        -1.6f, -3.5f,-4.5f, -0.8f, -0.7f, 4.2f,
                        -0.9f, -1.3f
                };
                break;
            case "Hopp-Woods":
                weights = new float[] {
                        -0.5f, -1f, 3f, 3f, -2.5f, 0f, -0.5f,
                        -1.8f, 3f, -1.8f, -1.3f, 0.2f, 0f, 0.2f,
                        3f, 0.3f, -0.4f, -1.5f, -3.4f, -2.3f
                };
                break;
            case "Cornette":
                weights = new float[] {
                        0.2f, 4.1f, -3.1f, -1.8f, 4.4f, 0f, 0.5f,
                        4.8f, -3.1f, 5.7f, 4.2f, -0.5f, -2.2f,
                        -2.8f, 1.4f, -0.5f, -1.9f, 4.7f, 1f, 3.2f
                };
                break;
            case "Eisenberg":
                weights = new float[] {
                        0.62f, 0.29f, -0.9f, -0.74f, 1.19f, 0.48f,
                        -0.4f, 1.38f, -1.5f, 1.06f, 0.64f, -0.78f,
                        0.12f, -0.85f, -2.53f, -0.18f, -0.05f, 1.08f,
                        0.81f, 0.26f
                };
                break;
            case "Rose":
                weights = new float[] {
                        0.74f, 0.91f, 0.62f, 0.62f, 0.88f, 0.72f,
                        0.78f, 0.88f, 0.52f, 0.85f, 0.85f, 0.63f,
                        0.64f, 0.62f, 0.64f, 0.66f, 0.7f, 0.86f,
                        0.85f, 0.76f
                };
                break;
            case "Janin":
                weights = new float[] {
                        0.3f, 0.9f, -0.6f, -0.7f, 0.5f, 0.3f, -0.1f,
                        0.7f, -1.8f, 0.5f, 0.4f, -0.5f, -0.3f, -0.7f,
                        -1.4f, -0.1f, -0.2f, 0.6f, 0.3f, -0.4f
                };
                break;
            case "Engelman GES":
                weights = new float[] {
                        1.6f, 2f, -9.2f, -8.2f, 3.7f, 1f, -3f, 3.1f,
                        -8.8f, 2.8f, 3.4f, -4.8f, -0.2f, -4.1f, -12.3f,
                        0.6f, 1.2f, 2.6f, 1.9f, -0.7f
                };
                break;
        }
    }

    public String toString() {
        return name;
    }
}
