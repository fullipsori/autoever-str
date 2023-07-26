import tensorflow as tf
from tensorflow import keras
import pickle

filename = "d:/projects/from_hyuncar/OCSVM/OCSVM_model_pickle"
with open(filename, 'rb') as f:
    pickle_loaded = pickle.load(f) # deserialize using load()
    print(pickle_loaded)

model = tf.keras.models.load_model("d:/projects/from_hyuncar/OCSVM/model2(autoencoder_inputshape_xx_200_19)/")
Summary = model.summary()