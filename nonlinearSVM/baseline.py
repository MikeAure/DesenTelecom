import pandas as pd
from sklearn.svm import SVC
from imblearn.over_sampling import SMOTE
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, f1_score

pd.set_option('display.max_columns', None)

random_state = 17
scaling_factor = 2 ** 20

df = pd.read_csv('diabetes.csv', delimiter=',', encoding='utf-8')
X = df.drop('Outcome', axis=1)
y = df['Outcome']

y = y.replace({0: -1})
scaler = MinMaxScaler()
for column in X.columns:
    X[column] = scaler.fit_transform(X[[column]])

smote = SMOTE(sampling_strategy={-1: 3000, 1: 3000}, random_state=random_state)
X_resample, y_resample = smote.fit_resample(X, y)

X_resample *= scaling_factor
X_resample = X_resample.astype(int)

X_train, X_test, y_train, y_test = train_test_split(X_resample, y_resample, test_size=0.2, shuffle=True,
                                                    random_state=random_state)

clf = SVC(kernel='rbf', random_state=random_state)
clf.fit(X_train, y_train)
y_preds = clf.predict(X_test)

accuracy = accuracy_score(y_test, y_preds)
print(f"Accuracy on test set: {accuracy * 100:.2f}%")
f1 = f1_score(y_test, y_preds)
print(f"F1 score on test set: {f1 * 100:.2f}%\n")

train_data = X_train.assign(Outcome=y_train)
test_data = X_test.assign(Outcome=y_test)

train_data.to_csv('train_data.csv', index=False)
test_data.to_csv('test_data.csv', index=False)

chunk_size = 300
chunks = [test_data[i:i + chunk_size] for i in range(0, len(test_data), chunk_size)]
for i, chunk in enumerate(chunks):
    features = chunk.iloc[:, :-1]
    labels = chunk.iloc[:, -1]
    features.to_csv(f'test_features_part_{i+1}.csv', index=False)
    labels.to_csv(f'test_labels_part_{i+1}.csv', index=False)
