# -*- coding: utf-8 -*-
from keras.layers.core import Activation, Dense
from keras.layers.embeddings import Embedding
from keras.layers.recurrent import LSTM
from keras.models import Sequential
from keras.preprocessing import sequence
from keras import optimizers
from sklearn.model_selection import train_test_split
import collections
import nltk
import numpy as np
from keras.utils import np_utils
import pickle
from keras.models import load_model
from sklearn.metrics import classification_report, confusion_matrix
from tensorflow.keras import backend
from nltk.stem import PorterStemmer # Stemming words
import matplotlib.pyplot as plt
from itertools import product
import json
from keras.models import load_model



# Prepare data for LSTM Construct
maxlen = 0
word_freqs = collections.Counter()
num_recs = 0
sentences = []
with open('./Jan9-2012-tweets-clean.txt','r+',encoding='UTF-8') as f:
    for line in f:
        chunks = line.strip().split("\t")
        if len(chunks) == 3:
            userID = chunks[0]
            sentence = chunks[1]
            labelStr = chunks[2][3:]
            sentences.append(sentence)
            words = nltk.word_tokenize(sentence.lower())
            if len(words) > maxlen:
                maxlen = len(words)
            for word in words:
                ps = ps = PorterStemmer()
                word = ps.stem(word)
                word_freqs[word] += 1
            num_recs += 1
print('max_len ',maxlen)
print('nb_words ', len(word_freqs))

def plot_confusion_matrix(cm, labels,
                          normalize=True,
                          title='Confusion Matrix (Validation Set)',
                          cmap=plt.cm.Blues):
    """
    This function prints and plots the confusion matrix.
    Normalization can be applied by setting `normalize=True`.
    """
    if normalize:
        cm = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]
        #print("Normalized confusion matrix")
    else:
        #print('Confusion matrix, without normalization')
        pass

    #print(cm)

    plt.imshow(cm, interpolation='nearest', cmap=cmap)
    plt.title(title)
    plt.colorbar()
    tick_marks = np.arange(len(labels))
    plt.xticks(tick_marks, labels, rotation=45)
    plt.yticks(tick_marks, labels)

    fmt = '.2f' if normalize else 'd'
    thresh = cm.max() / 2.
    for i, j in product(range(cm.shape[0]), range(cm.shape[1])):
        plt.text(j, i, format(cm[i, j], fmt),
                 horizontalalignment="center",
                 color="white" if cm[i, j] > thresh else "black")

    plt.tight_layout()
    plt.ylabel('True label')
    plt.xlabel('Predicted label')
    plt.savefig('confusion_matrix.png')

# Prepare data in order to train
MAX_FEATURES = 20000
MAX_SENTENCE_LENGTH = 60
vocab_size = min(MAX_FEATURES, len(word_freqs)) + 2
word2index = {x[0]: i+2 for i, x in enumerate(word_freqs.most_common(MAX_FEATURES))}
word2index["PAD"] = 0
word2index["UNK"] = 1
index2word = {v:k for k, v in word2index.items()} # map word to index
X = np.empty(num_recs,dtype=list)
y = []
i=0
# append X and Y
with open('./Jan9-2012-tweets-clean.txt','r+',encoding='UTF-8') as f:
    for line in f:
        chunks = line.strip().split("\t")
        if len(chunks) == 3:
            userID = chunks[0]
            sentence = chunks[1]
            labelStr = chunks[2][3:]
            label = -1
            if labelStr == "joy":
                label = 0
            elif labelStr == "sadness":
                label = 1
            elif labelStr == "surprise":
                label = 2
            elif labelStr == "disgust":
                label = 3
            elif labelStr == "fear":
                label = 4
            elif labelStr == "anger":
                label = 5
            label = np_utils.to_categorical(label,6)
            # print(label)
            words = nltk.word_tokenize(sentence.lower())
            seqs = []
            for word in words:
                # commonly used stemmer
                ps = PorterStemmer()
                word = ps.stem(word)
                if word in word2index:
                    seqs.append(word2index[word])
                else:
                    seqs.append(word2index["UNK"])
            X[i] = seqs
            # y[i] = label
            y.append(label)
            i += 1
# y = np.array(y)
# X = sequence.pad_sequences(X, maxlen=MAX_SENTENCE_LENGTH)
# # Divide dataset to traning set and test set
# Xtrain, Xtest, ytrain, ytest = train_test_split(X, y, test_size=0.2, random_state=42)
# # Construct Network
# EMBEDDING_SIZE = 256
# HIDDEN_LAYER_SIZE = 128
# BATCH_SIZE = 128
# NUM_EPOCHS = 16
# model = Sequential()
# model.add(Embedding(vocab_size, EMBEDDING_SIZE,input_length=MAX_SENTENCE_LENGTH))
# model.add(LSTM(HIDDEN_LAYER_SIZE, dropout=0.2, recurrent_dropout=0.2)) # Using LSTM network
# model.add(Dense(6))
# model.add(Activation("softmax")) # Using softmax as activation method
# adam = optimizers.Adam(lr=1, beta_1=0.9, beta_2=0.999, epsilon=None, decay=0.5, amsgrad=False) # Adam optimizer
# model.compile(loss="categorical_crossentropy", optimizer='adam',metrics=["accuracy"])
# ## Network Training
# model.fit(Xtrain, ytrain, batch_size=BATCH_SIZE, epochs=NUM_EPOCHS, validation_data=(Xtest, ytest))
# ## Predict
# y_pred = model.predict(Xtest, batch_size=BATCH_SIZE)
# score, acc = model.evaluate(Xtest, ytest, batch_size=BATCH_SIZE) # Evaluation
# y_pred_classes = []
# Y_test = []
# for y_test in ytest:
#     index_of_maximum = np.argmax(y_test)
#     Y_test.append(index_of_maximum)
# for predict in y_pred:
#     index_of_maximum = np.argmax(predict)
#     y_pred_classes.append(index_of_maximum)
# classes = ['joy', 'sadness', 'surprise', 'disgust', 'fear', 'anger']
# # see classification_report
# print(classification_report(Y_test, y_pred_classes, target_names=classes))
# cnf_matrix = confusion_matrix(Y_test, y_pred_classes)
# plt.figure(figsize=(20,10))
# plot_confusion_matrix(cnf_matrix, labels=classes)
# model.save('my_model.h5')

### Examples
# print("\nTest score: %.3f, accuracy: %.3f" % (score, acc))
# print('{}   {}      {}'.format('Prediction','True Value','Sentence'))
# for i in range(5):
#     idx = np.random.randint(len(Xtest))
#     xtest = Xtest[idx].reshape(1,60)
#     ylabel = ytest[idx]
#     ypred = model.predict(xtest)[0]
#     sent = " ".join([index2word[x] for x in xtest[0] if x != 0])
#     print(' {}      {}     {}'.format(ypred, ylabel, sent))
model = load_model('my_model.h5')
##### Input by ourselves
tweet_dict = ''
with open("newyork_tweets.json") as f:
    tweet_dict = json.load(f)
INPUT_SENTENCES = []
inputSentence = '';
for tweet in tweet_dict:
    INPUT_SENTENCES.append(tweet['text'])

XX = np.empty(len(INPUT_SENTENCES),dtype=list)
i=0
for sentence in  INPUT_SENTENCES:
    words = nltk.word_tokenize(sentence.lower())
    seq = []
    for word in words:
        ps = PorterStemmer()
        word = ps.stem(word)
        if word in word2index:
            seq.append(word2index[word])
        else:
            seq.append(word2index['UNK'])
    XX[i] = seq
    i+=1
emotion_dict = {
    'anger' : 0,
    'fear' : 0,
    'disgust' : 0,
    'surprise' : 0,
    'sadness' : 0,
    'joy' : 0
}
XX = sequence.pad_sequences(XX, maxlen=MAX_SENTENCE_LENGTH)
labels = [x for x in model.predict(XX) ]
label2word = {5:'anger',4:'fear',3:'disgust',2:'surprise',1:'sadness', 0:'joy'}
for i in range(len(INPUT_SENTENCES)):
    p = labels[i].tolist().index(max(labels[i].tolist()))
    emotion_dict[label2word[p]] += 1
    print('{}   {}'.format(label2word[p], INPUT_SENTENCES[i]))
with open('emotion_sum_newyork.json', 'w') as json_file:
    json.dump(emotion_dict, json_file)