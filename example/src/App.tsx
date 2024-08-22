import { useState, useEffect } from 'react';
import { StyleSheet, View, Text } from 'react-native';
import { containsSignatures, isRooted } from 'react-native-security-pack';

export default function App() {
  const [result, setResult] = useState<any>({});

  useEffect(() => {
    containsSignatures(['5E8F16062EA3CD2C4A0D547876BAA6F38CABF625']).then((r) =>
      setResult((value: any) => ({
        ...value,
        containsSignatures: r,
      }))
    );
    isRooted().then((rooted) =>
      setResult((value: any) => ({
        ...value,
        rooted,
      }))
    );
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {JSON.stringify(result)}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
